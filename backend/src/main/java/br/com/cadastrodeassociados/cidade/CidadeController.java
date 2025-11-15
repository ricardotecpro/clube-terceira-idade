package br.com.cadastrodeassociados.cidade;

import br.com.cadastrodeassociados.cidade.dto.CidadeRequestDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cidades")
public class CidadeController {

    private static final Logger logger = LoggerFactory.getLogger(CidadeController.class);

    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<List<Cidade>> buscarCidades(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isBlank()) {
            return ResponseEntity.ok(cidadeRepository.findByNomeContainingIgnoreCase(nome));
        }
        return ResponseEntity.ok(cidadeRepository.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<Cidade> buscarCidadePorId(@PathVariable Long id) {
        Optional<Cidade> cidade = cidadeRepository.findById(id);
        return cidade.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<Cidade> criarCidade(@Valid @RequestBody CidadeRequestDTO cidadeDTO) {
        logger.info("Recebendo requisição para criar cidade: {}", cidadeDTO);
        try {
            Cidade novaCidade = new Cidade();
            novaCidade.setNome(cidadeDTO.getNome());
            novaCidade.setEstado(cidadeDTO.getEstado());
            Cidade cidadeSalva = cidadeRepository.save(novaCidade);
            logger.info("Cidade salva com sucesso: {}", cidadeSalva);
            return ResponseEntity.status(HttpStatus.CREATED).body(cidadeSalva);
        } catch (Exception e) {
            logger.error("Erro ao salvar a cidade: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<Cidade> atualizarCidade(@PathVariable Long id, @Valid @RequestBody CidadeRequestDTO cidadeDTO) {
        return cidadeRepository.findById(id)
                .map(cidadeExistente -> {
                    cidadeExistente.setNome(cidadeDTO.getNome());
                    cidadeExistente.setEstado(cidadeDTO.getEstado());
                    Cidade cidadeAtualizada = cidadeRepository.save(cidadeExistente);
                    return ResponseEntity.ok(cidadeAtualizada);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode excluir
    public ResponseEntity<Void> deletarCidade(@PathVariable Long id) {
        if (!cidadeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        cidadeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
