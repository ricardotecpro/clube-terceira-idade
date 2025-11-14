package br.com.cadastrodeassociados.pagamento;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import br.com.cadastrodeassociados.pagamento.dto.PagamentoRequestDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importar
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoControler {
    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private AssociadoRepository associadoRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')") // Apenas ADMIN e SECRETARIO podem listar
    public List<Pagamento> listarTodosPagamentos() {
        return pagamentoRepository.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')") // Apenas ADMIN e SECRETARIO podem ver detalhes
    public ResponseEntity<Pagamento> buscarPagamentoPorId(@PathVariable Long id) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);
        return pagamento.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/associado/{associadoId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')") // Apenas ADMIN e SECRETARIO podem listar por associado
    public List<Pagamento> listarPagamentosPorAssociado(@PathVariable Long associadoId) {
        return pagamentoRepository.findByAssociadoIdOrderByDataPagamentoDesc(associadoId);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')") // Apenas ADMIN e SECRETARIO podem criar
    public ResponseEntity<Pagamento> salvarPagamento(@Valid @RequestBody PagamentoRequestDTO pagamentoDTO) {
        Associado associado = associadoRepository.findById(pagamentoDTO.getAssociadoId())
                .orElseThrow(() -> new RuntimeException("Associado não encontrado!"));

        Pagamento pagamento = new Pagamento();
        pagamento.setAssociado(associado);
        pagamento.setValor(pagamentoDTO.getValor());
        pagamento.setDataPagamento(pagamentoDTO.getDataPagamento());
        pagamento.setTipoPagamento(pagamentoDTO.getTipoPagamento());
        pagamento.setFormaPagamento(pagamentoDTO.getFormaPagamento());

        Pagamento novoPagamento = pagamentoRepository.save(pagamento);
        return ResponseEntity.ok(novoPagamento);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')") // Apenas ADMIN e SECRETARIO podem atualizar
    public ResponseEntity<Pagamento> atualizarPagamento(@PathVariable Long id, @Valid @RequestBody PagamentoRequestDTO pagamentoDTO) {
        return pagamentoRepository.findById(id)
                .map(pagamentoExistente -> {
                    pagamentoExistente.setValor(pagamentoDTO.getValor());
                    pagamentoExistente.setDataPagamento(pagamentoDTO.getDataPagamento());
                    pagamentoExistente.setTipoPagamento(pagamentoDTO.getTipoPagamento());
                    pagamentoExistente.setFormaPagamento(pagamentoDTO.getFormaPagamento());

                    Pagamento updatedPagamento = pagamentoRepository.save(pagamentoExistente);
                    return ResponseEntity.ok(updatedPagamento);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
