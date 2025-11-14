package br.com.cadastrodeassociados.mensagens;

import br.com.cadastrodeassociados.mensagens.dto.MensagemRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mensagens")
public class MensagemController {

    @Autowired
    private MensagemService mensagemService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode enviar mensagens
    public ResponseEntity<Mensagem> enviarMensagem(@Valid @RequestBody MensagemRequestDTO mensagemDTO) {
        try {
            Mensagem novaMensagem = mensagemService.enviarMensagem(mensagemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaMensagem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null); // Ou um DTO de erro mais específico
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode listar mensagens
    public ResponseEntity<List<Mensagem>> listarMensagens() {
        List<Mensagem> mensagens = mensagemService.listarMensagens();
        return ResponseEntity.ok(mensagens);
    }
}
