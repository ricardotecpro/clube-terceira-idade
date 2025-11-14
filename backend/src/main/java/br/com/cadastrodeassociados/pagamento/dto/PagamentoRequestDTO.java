package br.com.cadastrodeassociados.pagamento.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PagamentoRequestDTO {

    @NotNull(message = "O valor é obrigatório.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @NotNull(message = "A data de pagamento é obrigatória.")
    @PastOrPresent(message = "A data de pagamento não pode ser futura.")
    private LocalDate dataPagamento;

    private String tipoPagamento; // Pode ser nulo, ex: "Mensalidade"

    private String formaPagamento; // Pode ser nulo, ex: "Dinheiro", "Cartão"

    @NotNull(message = "O ID do associado é obrigatório.")
    private Long associadoId;
}
