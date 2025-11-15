package br.com.cadastrodeassociados.associado;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AssociadoRepositoryCustomImpl implements AssociadoRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Associado> findComFiltros(String bairro, String escolaridade, Integer idadeMin, Integer idadeMax) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Associado> query = cb.createQuery(Associado.class);
        Root<Associado> associado = query.from(Associado.class);

        List<Predicate> predicates = new ArrayList<>();

        if (bairro != null && !bairro.isBlank()) {
            predicates.add(cb.like(cb.lower(associado.get("endereco")), "%" + bairro.toLowerCase() + "%"));
        }

        if (escolaridade != null && !escolaridade.isBlank()) {
            predicates.add(cb.equal(cb.lower(associado.get("escolaridade")), escolaridade.toLowerCase()));
        }

        if (idadeMin != null) {
            LocalDate dataNascimentoMax = LocalDate.now().minusYears(idadeMin);
            predicates.add(cb.lessThanOrEqualTo(associado.get("dataNascimento"), dataNascimentoMax));
        }

        if (idadeMax != null) {
            LocalDate dataNascimentoMin = LocalDate.now().minusYears(idadeMax + 1).plusDays(1);
            predicates.add(cb.greaterThanOrEqualTo(associado.get("dataNascimento"), dataNascimentoMin));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
