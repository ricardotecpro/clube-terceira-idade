package br.com.cadastrodeassociados.evento;

import br.com.cadastrodeassociados.evento.dto.EventoRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    @Autowired
    private EventoRepository eventoRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public List<Evento> getAllEventos() {
        return eventoRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Evento> createEvento(@Valid @RequestBody EventoRequestDTO eventoDTO) {
        Evento evento = new Evento();
        evento.setNome(eventoDTO.getNome());
        evento.setData(eventoDTO.getData());
        evento.setHora(eventoDTO.getHora()); // Novo campo
        evento.setLocal(eventoDTO.getLocal()); // Novo campo
        evento.setDescricao(eventoDTO.getDescricao());
        evento.setParticipantes(eventoDTO.getParticipantes()); // Novo campo

        Evento novoEvento = eventoRepository.save(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEvento);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Evento> updateEvento(@PathVariable Long id, @Valid @RequestBody EventoRequestDTO eventoDTO) {
        return eventoRepository.findById(id)
                .map(eventoExistente -> {
                    eventoExistente.setNome(eventoDTO.getNome());
                    eventoExistente.setData(eventoDTO.getData());
                    eventoExistente.setHora(eventoDTO.getHora()); // Novo campo
                    eventoExistente.setLocal(eventoDTO.getLocal()); // Novo campo
                    eventoExistente.setDescricao(eventoDTO.getDescricao());
                    eventoExistente.setParticipantes(eventoDTO.getParticipantes()); // Novo campo

                    final Evento updatedEvento = eventoRepository.save(eventoExistente);
                    return ResponseEntity.ok(updatedEvento);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvento(@PathVariable Long id) {
        if (!eventoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
