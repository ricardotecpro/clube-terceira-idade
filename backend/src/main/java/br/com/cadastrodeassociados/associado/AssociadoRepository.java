package br.com.cadastrodeassociados.associado;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    List<Associado> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT a FROM Associado a WHERE " +
            "(:nome IS NULL OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:cpf IS NULL OR a.cpf LIKE %:cpf%) AND " +
            "(:situacao IS NULL OR a.situacao = :situacao) AND " +
            "(:bairro IS NULL OR a.endereco LIKE %:bairro%) AND " +
            "(:escolaridade IS NULL OR a.escolaridade = :escolaridade)")
    List<Associado> findWithAdvancedFilters(@Param("nome") String nome,
                                            @Param("cpf") String cpf,
                                            @Param("situacao") String situacao,
                                            @Param("bairro") String bairro,
                                            @Param("escolaridade") String escolaridade);

    @Query("SELECT a FROM Associado a WHERE " +
           "(:dataNascimentoMin IS NULL OR a.dataNascimento <= :dataNascimentoMin) AND " +
           "(:dataNascimentoMax IS NULL OR a.dataNascimento >= :dataNascimentoMax)")
    List<Associado> findByDataNascimentoBetween(
            @Param("dataNascimentoMin") LocalDate dataNascimentoMin,
            @Param("dataNascimentoMax") LocalDate dataNascimentoMax);

    long countBySituacao(String situacao);
}
