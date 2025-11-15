package br.com.cadastrodeassociados.associado;

import java.util.List;

public interface AssociadoRepositoryCustom {
    List<Associado> findComFiltros(String bairro, String escolaridade, Integer idadeMin, Integer idadeMax);
}
