package br.com.cadastrodeassociados.cidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List; // Importar List
import java.util.Optional;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    Optional<Cidade> findByNome(String nome);
    List<Cidade> findByNomeContainingIgnoreCase(String nome); // Adicionado para autocompletar
}
