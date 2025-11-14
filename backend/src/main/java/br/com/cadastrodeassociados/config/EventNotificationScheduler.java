package br.com.cadastrodeassociados.config;

import br.com.cadastrodeassociados.associado.AssociadoRepository; // Importar
import br.com.cadastrodeassociados.evento.Evento;
import br.com.cadastrodeassociados.evento.EventoRepository;
import br.com.cadastrodeassociados.mensagens.MensagemService;
import br.com.cadastrodeassociados.mensagens.dto.MensagemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventNotificationScheduler {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private MensagemService mensagemService;

    @Autowired
    private AssociadoRepository associadoRepository; // Injetar

    // Executa diariamente à meia-noite
    @Scheduled(cron = "0 0 0 * * ?")
    public void verificarEnotificarEventosProximos() {
        LocalDate hoje = LocalDate.now();
        LocalDate dataMinNotificacao = hoje.plusDays(3); // Notificar 3 dias antes
        LocalDate dataMaxNotificacao = hoje.plusDays(5); // Até 5 dias antes

        List<Evento> eventosProximos = eventoRepository.findAll().stream()
                .filter(evento -> evento.getData().isAfter(hoje) &&
                                 !evento.getData().isBefore(dataMinNotificacao) &&
                                 !evento.getData().isAfter(dataMaxNotificacao))
                .collect(Collectors.toList());

        if (!eventosProximos.isEmpty()) {
            System.out.println("Verificando eventos próximos para notificação...");
            for (Evento evento : eventosProximos) {
                // Para simplificar, vamos notificar todos os associados cadastrados no sistema
                List<Long> todosAssociadosIds = associadoRepository.findAll().stream() // Usar associadoRepository
                                                    .map(associado -> associado.getId())
                                                    .collect(Collectors.toList());

                if (!todosAssociadosIds.isEmpty()) {
                    String conteudoMensagem = String.format(
                            "Lembrete: O evento '%s' acontecerá em %s. Descrição: %s",
                            evento.getNome(), evento.getData().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), evento.getDescricao()
                    );

                    MensagemRequestDTO mensagemDTO = new MensagemRequestDTO();
                    mensagemDTO.setConteudo(conteudoMensagem);
                    mensagemDTO.setTipo("SISTEMA"); // Notificação interna do sistema
                    mensagemDTO.setDestinatarioIds(todosAssociadosIds);

                    try {
                        mensagemService.enviarMensagem(mensagemDTO);
                        System.out.println("Notificação enviada para o evento: " + evento.getNome());
                    } catch (Exception e) {
                        System.err.println("Erro ao enviar notificação para o evento " + evento.getNome() + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}
