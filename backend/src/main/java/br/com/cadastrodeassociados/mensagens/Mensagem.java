package br.com.cadastrodeassociados.mensagens;

import br.com.cadastrodeassociados.associado.Associado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String conteudo;

    @Column(nullable = false, length = 100)
    private String titulo; // Adicionado o campo titulo

    @Column(nullable = false)
    private String tipo; // Ex: "EMAIL", "SMS", "SISTEMA"

    @Column(nullable = false)
    private LocalDateTime dataEnvio;

    @ManyToMany
    @JoinTable(
        name = "mensagem_associado",
        joinColumns = @JoinColumn(name = "mensagem_id"),
        inverseJoinColumns = @JoinColumn(name = "associado_id")
    )
    private List<Associado> destinatarios;
}
