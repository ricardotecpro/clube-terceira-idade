package br.com.cadastrodeassociados.mensagens.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MensagemRequestDTO {

    @NotBlank(message = "O título da mensagem é obrigatório.")
    @Size(min = 1, max = 100, message = "O título deve ter entre 1 e 100 caracteres.")
    private String titulo;

    @NotBlank(message = "O conteúdo da mensagem é obrigatório.")
    @Size(min = 1, max = 500, message = "O conteúdo deve ter entre 1 e 500 caracteres.")
    private String conteudo;

    @NotBlank(message = "O tipo da mensagem é obrigatório.")
    @Pattern(regexp = "EMAIL|SMS|SISTEMA", message = "O tipo deve ser 'EMAIL', 'SMS' ou 'SISTEMA'.")
    private String tipo;

    @NotEmpty(message = "Pelo menos um destinatário deve ser selecionado.")
    private List<Long> destinatarioIds;
}
