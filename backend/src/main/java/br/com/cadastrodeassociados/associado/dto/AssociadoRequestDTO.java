package br.com.cadastrodeassociados.associado.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssociadoRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF inválido. Use o formato XXX.XXX.XXX-XX.")
    private String cpf;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @PastOrPresent(message = "A data de nascimento não pode ser futura.")
    private LocalDate dataNascimento;

    @NotBlank(message = "A situação é obrigatória.")
    @Pattern(regexp = "Adimplente|Inadimplente", message = "A situação deve ser 'Adimplente' ou 'Inadimplente'.")
    private String situacao;

    @NotNull(message = "O ID da cidade é obrigatório.")
    private Long cidadeId;
}
