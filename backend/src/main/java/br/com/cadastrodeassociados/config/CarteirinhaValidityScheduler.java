package br.com.cadastrodeassociados.config;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import br.com.cadastrodeassociados.carteira.Carteirinha;
import br.com.cadastrodeassociados.carteira.CarteirinhaRepository;
import br.com.cadastrodeassociados.mensagens.MensagemService;
import br.com.cadastrodeassociados.mensagens.dto.MensagemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CarteirinhaValidityScheduler {

    @Autowired
    private CarteirinhaRepository carteirinhaRepository;

    @Autowired
    private AssociadoRepository associadoRepository; // Para notificar o secretário (se for um associado específico)

    @Autowired
    private MensagemService mensagemService;

    // Executa diariamente à meia-noite
    @Scheduled(cron = "0 0 0 * * ?")
    public void verificarValidadeCarteirinhas() {
        LocalDate hoje = LocalDate.now();
        LocalDate proximoMes = hoje.plusMonths(1); // Para notificar com 1 mês de antecedência

        List<Carteirinha> carteirinhas = carteirinhaRepository.findAll();

        for (Carteirinha carteirinha : carteirinhas) {
            Associado associado = carteirinha.getAssociado();
            if (associado == null) continue;

            // Notificar associado em caso de vencimento
            if (carteirinha.getDataValidade().isBefore(hoje)) {
                String conteudoAssociado = String.format(
                        "Sua carteirinha de associado venceu em %s. Por favor, procure a secretaria para renovação.",
                        carteirinha.getDataValidade().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
                enviarNotificacao(associado.getId(), conteudoAssociado, "EMAIL"); // Ou SMS
                System.out.println("Notificação de vencimento enviada para " + associado.getNome());
            }
            // Notificar secretário em caso de proximidade do vencimento (1 mês)
            else if (carteirinha.getDataValidade().isAfter(hoje) && carteirinha.getDataValidade().isBefore(proximoMes)) {
                String conteudoSecretario = String.format(
                        "A carteirinha do associado %s (ID: %d) vencerá em %s. Favor entrar em contato para renovação.",
                        associado.getNome(), associado.getId(), carteirinha.getDataValidade().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
                // Enviar para um ID de secretário predefinido ou para todos os ADMINs/SECRETARIOs
                // Por simplicidade, vamos logar e assumir que um ADMIN/SECRETARIO verá
                System.out.println("Notificação para secretário: " + conteudoSecretario);
                // Em um cenário real, buscaria o ID do usuário secretário ou um grupo de usuários
                // List<Long> secretarioIds = userRepository.findByRole("SECRETARIO").stream().map(User::getId).collect(Collectors.toList());
                // enviarNotificacao(secretarioIds, conteudoSecretario, "SISTEMA");
            }
        }
    }

    private void enviarNotificacao(Long associadoId, String conteudo, String tipo) {
        MensagemRequestDTO mensagemDTO = new MensagemRequestDTO();
        mensagemDTO.setConteudo(conteudo);
        mensagemDTO.setTipo(tipo);
        mensagemDTO.setDestinatarioIds(Collections.singletonList(associadoId)); // Apenas o associado

        try {
            mensagemService.enviarMensagem(mensagemDTO);
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
        }
    }
}
