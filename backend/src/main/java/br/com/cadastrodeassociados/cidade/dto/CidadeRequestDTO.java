package br.com.cadastrodeassociados.cidade.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CidadeRequestDTO {

    @NotBlank(message = "O nome da cidade é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome da cidade deve ter entre 2 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O estado (UF) é obrigatório.")
    @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres (UF).")
    private String estado;
}
