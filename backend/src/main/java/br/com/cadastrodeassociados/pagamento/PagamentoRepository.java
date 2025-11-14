package br.com.cadastrodeassociados.pagamento;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByAssociadoIdOrderByDataPagamentoDesc(Long associadoId);

    // Adicionado para o Dashboard
    long countByFormaPagamentoIsNull();
}
