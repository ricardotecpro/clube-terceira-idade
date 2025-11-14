package br.com.cadastrodeassociados.mensagens;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import br.com.cadastrodeassociados.mensagens.dto.MensagemRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MensagemService {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    public Mensagem enviarMensagem(MensagemRequestDTO mensagemDTO) {
        List<Associado> destinatarios = associadoRepository.findAllById(mensagemDTO.getDestinatarioIds());

        if (destinatarios.isEmpty()) {
            throw new RuntimeException("Nenhum destinatário válido encontrado.");
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setConteudo(mensagemDTO.getConteudo());
        mensagem.setTipo(mensagemDTO.getTipo());
        mensagem.setDataEnvio(LocalDateTime.now());
        mensagem.setDestinatarios(destinatarios);

        // Lógica de envio real (e-mail, SMS) seria integrada aqui
        System.out.println("Enviando mensagem do tipo " + mensagem.getTipo() + " para " + destinatarios.size() + " associados:");
        destinatarios.forEach(a -> System.out.println(" - " + a.getNome() + " (" + a.getEmail() + ")"));
        System.out.println("Conteúdo: " + mensagem.getConteudo());

        return mensagemRepository.save(mensagem);
    }

    public List<Mensagem> listarMensagens() {
        return mensagemRepository.findAll();
    }
}
