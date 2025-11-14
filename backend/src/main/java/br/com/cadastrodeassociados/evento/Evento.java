package br.com.cadastrodeassociados.evento;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime; // Importar LocalTime

@Entity
@Getter
@Setter
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private LocalDate data;
    private LocalTime hora; // Novo campo
    private String local; // Novo campo
    private String descricao;
    private String participantes; // Novo campo (simplificado como String)
}
