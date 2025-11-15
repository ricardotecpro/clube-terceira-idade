package br.com.cadastrodeassociados.dashboard;

import br.com.cadastrodeassociados.associado.AssociadoRepository;
import br.com.cadastrodeassociados.pagamento.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @GetMapping("/metrics")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public ResponseEntity<Map<String, Object>> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // Total de Associados
        metrics.put("totalAssociados", associadoRepository.count());

        // Associados Adimplentes e Inadimplentes
        metrics.put("associadosAdimplentes", associadoRepository.countBySituacao("Adimplente"));
        metrics.put("associadosInadimplentes", associadoRepository.countBySituacao("Inadimplente"));

        // Pagamentos Pendentes (simplificado: pagamentos sem forma de pagamento)
        metrics.put("pagamentosPendentes", pagamentoRepository.countByFormaPagamentoIsNull());

        // Aniversariantes do Mês
        LocalDate hoje = LocalDate.now();
        int mesAtual = hoje.getMonthValue();
        metrics.put("aniversariantesMes", associadoRepository.countByMesAniversario(mesAtual));

        return ResponseEntity.ok(metrics);
    }
}
