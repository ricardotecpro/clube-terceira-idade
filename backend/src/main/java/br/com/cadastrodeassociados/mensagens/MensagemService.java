package br.com.cadastrodeassociados.mensagens;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import br.com.cadastrodeassociados.mensagens.dto.MensagemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private JavaMailSender mailSender;

    public Mensagem enviarMensagem(MensagemRequestDTO mensagemDTO) {
        List<Associado> destinatarios = associadoRepository.findAllById(mensagemDTO.getDestinatarioIds());

        if (destinatarios.isEmpty()) {
            throw new RuntimeException("Nenhum destinatário válido encontrado.");
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setTitulo(mensagemDTO.getTitulo());
        mensagem.setConteudo(mensagemDTO.getConteudo());
        mensagem.setTipo(mensagemDTO.getTipo());
        mensagem.setDataEnvio(LocalDateTime.now());
        mensagem.setDestinatarios(destinatarios);

        // Envio real do e-mail
        if ("EMAIL".equalsIgnoreCase(mensagem.getTipo())) {
            for (Associado associado : destinatarios) {
                if (associado.getEmail() != null && !associado.getEmail().isBlank()) {
                    sendEmail(associado.getEmail(), mensagem.getTitulo(), mensagem.getConteudo());
                }
            }
        }

        return mensagemRepository.save(mensagem);
    }

    private void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            // message.setFrom("nao-responda@meudominio.com"); // Opcional: configurar um remetente padrão
            mailSender.send(message);
        } catch (Exception e) {
            // Em um cenário real, usar um logger e talvez uma fila de re-tentativa
            System.err.println("Erro ao enviar e-mail para " + to + ": " + e.getMessage());
        }
    }

    public List<Mensagem> listarMensagens() {
        return mensagemRepository.findAll();
    }
}
