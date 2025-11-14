package br.com.cadastrodeassociados.carteira;

import br.com.cadastrodeassociados.associado.Associado;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Carteirinha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroCarteirinha;

    private LocalDate dataValidade;

    @OneToOne
    @JoinColumn(name = "associado_id", unique = true, nullable = false)
    private Associado associado;
}
