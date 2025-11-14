package br.com.cadastrodeassociados.evento.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime; // Importar LocalTime

@Getter
@Setter
public class EventoRequestDTO {

    @NotBlank(message = "O nome do evento é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome do evento deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotNull(message = "A data do evento é obrigatória.")
    @FutureOrPresent(message = "A data do evento não pode ser passada.")
    private LocalDate data;

    @NotNull(message = "A hora do evento é obrigatória.") // Novo campo
    private LocalTime hora;

    @NotBlank(message = "O local do evento é obrigatório.") // Novo campo
    @Size(min = 3, max = 100, message = "O local deve ter entre 3 e 100 caracteres.")
    private String local;

    @Size(max = 500, message = "A descrição do evento não pode exceder 500 caracteres.")
    private String descricao;

    @Size(max = 255, message = "A lista de participantes não pode exceder 255 caracteres.") // Novo campo
    private String participantes;
}
