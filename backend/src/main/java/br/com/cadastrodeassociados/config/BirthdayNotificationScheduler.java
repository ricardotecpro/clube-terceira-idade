package br.com.cadastrodeassociados.config;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
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
public class BirthdayNotificationScheduler {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private MensagemService mensagemService;

    // Executa diariamente às 08:00 da manhã
    @Scheduled(cron = "0 0 8 * * ?")
    public void notificarAniversariantes() {
        LocalDate hoje = LocalDate.now();
        int mesAtual = hoje.getMonthValue();
        int diaAtual = hoje.getDayOfMonth();

        List<Associado> aniversariantesDoDia = associadoRepository.findAll().stream()
                .filter(associado -> associado.getDataNascimento() != null &&
                                     associado.getDataNascimento().getMonthValue() == mesAtual &&
                                     associado.getDataNascimento().getDayOfMonth() == diaAtual)
                .collect(Collectors.toList());

        if (!aniversariantesDoDia.isEmpty()) {
            System.out.println("Verificando aniversariantes do dia para notificação...");
            for (Associado associado : aniversariantesDoDia) {
                String tituloMensagem = "Feliz Aniversário!";
                String conteudoMensagem = String.format(
                        "Olá, %s!\n\nO Clube da Terceira Idade de Assis deseja a você um feliz aniversário, com muita saúde, paz e alegria!\n\nAtenciosamente,\nA Diretoria.",
                        associado.getNome()
                );

                MensagemRequestDTO mensagemDTO = new MensagemRequestDTO();
                mensagemDTO.setTitulo(tituloMensagem);
                mensagemDTO.setConteudo(conteudoMensagem);
                mensagemDTO.setTipo("EMAIL");
                mensagemDTO.setDestinatarioIds(Collections.singletonList(associado.getId()));

                try {
                    mensagemService.enviarMensagem(mensagemDTO);
                    System.out.println("Mensagem de aniversário enviada para: " + associado.getNome());
                } catch (Exception e) {
                    System.err.println("Erro ao enviar mensagem de aniversário para " + associado.getNome() + ": " + e.getMessage());
                }
            }
        }
    }
}
