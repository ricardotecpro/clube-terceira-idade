package br.com.cadastrodeassociados.associado;

import br.com.cadastrodeassociados.associado.dto.AssociadoRequestDTO;
import br.com.cadastrodeassociados.carteira.CarteirinhaService;
import br.com.cadastrodeassociados.cidade.Cidade;
import br.com.cadastrodeassociados.cidade.CidadeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/associados")
public class AssociadoController {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private CarteirinhaService carteirinhaService;

    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<List<Associado>> listarAssociados(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String situacao,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String escolaridade) {
        List<Associado> associados = associadoRepository.findWithAdvancedFilters(nome, cpf, situacao, bairro, escolaridade);
        return ResponseEntity.ok(associados);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<Associado> obterAssociadoPorId(@PathVariable("id") Long id) {
        Optional<Associado> associadoOpt = associadoRepository.findById(id);
        return associadoOpt.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Associado> salvarAssociado(@Valid @RequestBody AssociadoRequestDTO associadoDTO) {
        Cidade cidade = cidadeRepository.findById(associadoDTO.getCidadeId())
                            .orElseThrow(() -> new RuntimeException("Cidade não encontrada!"));

        Associado associado = new Associado();
        associado.setNome(associadoDTO.getNome());
        associado.setCpf(associadoDTO.getCpf());
        associado.setDataNascimento(associadoDTO.getDataNascimento());
        associado.setSituacao(associadoDTO.getSituacao());
        associado.setCidade(cidade);

        carteirinhaService.gerenciarCriacaoCarteirinha(associado);
        Associado novoAssociado = associadoRepository.save(associado);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAssociado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Associado> atualizarAssociado(@PathVariable("id") Long id, @Valid @RequestBody AssociadoRequestDTO associadoDTO) {
        return associadoRepository.findById(id)
                .map(associadoExistente -> {
                    associadoExistente.setNome(associadoDTO.getNome());
                    associadoExistente.setCpf(associadoDTO.getCpf());
                    associadoExistente.setDataNascimento(associadoDTO.getDataNascimento());
                    associadoExistente.setSituacao(associadoDTO.getSituacao());

                    Cidade cidade = cidadeRepository.findById(associadoDTO.getCidadeId())
                                        .orElseThrow(() -> new RuntimeException("Cidade não encontrada!"));
                    associadoExistente.setCidade(cidade);

                    Associado salvo = associadoRepository.save(associadoExistente);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> apagarAssociado(@PathVariable("id") Long id) {
        if (!associadoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            associadoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{id}/renovar-carteirinha")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> renovarCarteirinha(
            @PathVariable Long id,
            @RequestParam LocalDate novaValidade) {
        try {
            carteirinhaService.renovarCarteirinha(id, novaValidade);
            return ResponseEntity.ok("Carteirinha renovada com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
