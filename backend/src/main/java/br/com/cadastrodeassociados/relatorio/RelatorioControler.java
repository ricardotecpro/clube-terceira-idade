package br.com.cadastrodeassociados.relatorio;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate; // Importar LocalDate
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioControler {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private AssociadoRepository associadoRepository;

    @GetMapping("/associados/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public void exportarPdfAssociados(
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String escolaridade,
            @RequestParam(required = false) Integer idadeMin, // Novo parametro
            @RequestParam(required = false) Integer idadeMax, // Novo parametro
            HttpServletResponse response) throws IOException, DocumentException {

        List<Associado> associadosFiltrados = buscarAssociadosComFiltros(bairro, escolaridade, idadeMin, idadeMax);

        response.setContentType("application/pdf");
        String headerValue = "attachment; filename=relatorio_associados_" + getCurrentDateTime() + ".pdf";
        response.setHeader("Content-Disposition", headerValue);

        relatorioService.gerarRelatorioPdfAssociados(response, associadosFiltrados);
    }

    @GetMapping("/associados/excel")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public void exportarExcelAssociados(
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String escolaridade,
            @RequestParam(required = false) Integer idadeMin, // Novo parametro
            @RequestParam(required = false) Integer idadeMax, // Novo parametro
            HttpServletResponse response) throws IOException {

        List<Associado> associadosFiltrados = buscarAssociadosComFiltros(bairro, escolaridade, idadeMin, idadeMax);

        response.setContentType("application/octet-stream");
        String headerValue = "attachment; filename=relatorio_associados_" + getCurrentDateTime() + ".xlsx";
        response.setHeader("Content-Disposition", headerValue);

        relatorioService.gerarRelatorioExcelAssociados(response, associadosFiltrados);
    }

    private List<Associado> buscarAssociadosComFiltros(String bairro, String escolaridade, Integer idadeMin, Integer idadeMax) {
        // Lógica para combinar os filtros
        // Por simplicidade, vamos buscar todos e depois filtrar em memória ou criar uma query mais complexa no repo
        List<Associado> associados = associadoRepository.findAll(); // Começa com todos

        // Filtra por bairro e escolaridade (se fornecidos)
        if (bairro != null && !bairro.isBlank() || escolaridade != null && !escolaridade.isBlank()) {
            // Se o findWithFilters for mais complexo, use-o. Aqui, vamos filtrar a lista atual
            associados = associados.stream()
                    .filter(a -> (bairro == null || (a.getEndereco() != null && a.getEndereco().contains(bairro))) &&
                                 (escolaridade == null || (a.getEscolaridade() != null && a.getEscolaridade().equalsIgnoreCase(escolaridade))))
                    .toList();
        }

        // Filtra por faixa etaria (se fornecida)
        if (idadeMin != null || idadeMax != null) {
            LocalDate hoje = LocalDate.now();
            LocalDate dataNascimentoMax = (idadeMin != null) ? hoje.minusYears(idadeMin) : null;
            LocalDate dataNascimentoMin = (idadeMax != null) ? hoje.minusYears(idadeMax + 1).plusDays(1) : null;

            // Usar o método do repositório para filtrar por data de nascimento
            // Ou filtrar a lista 'associados' em memória se a query do repo for muito restritiva
            associados = associados.stream()
                    .filter(a -> {
                        if (a.getDataNascimento() == null) return false;
                        boolean minOk = (dataNascimentoMin == null || !a.getDataNascimento().isAfter(dataNascimentoMin));
                        boolean maxOk = (dataNascimentoMax == null || !a.getDataNascimento().isBefore(dataNascimentoMax));
                        return minOk && maxOk;
                    })
                    .toList();
        }

        return associados;
    }

    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }
}
