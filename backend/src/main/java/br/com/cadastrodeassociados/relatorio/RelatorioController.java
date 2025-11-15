package br.com.cadastrodeassociados.relatorio;

import br.com.cadastrodeassociados.associado.Associado;
import br.com.cadastrodeassociados.associado.AssociadoRepository;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private AssociadoRepository associadoRepository;

    @GetMapping("/associados/pdf")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIO')")
    public void exportarPdfAssociados(
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String escolaridade,
            @RequestParam(required = false) Integer idadeMin,
            @RequestParam(required = false) Integer idadeMax,
            HttpServletResponse response) throws IOException, DocumentException {

        List<Associado> associadosFiltrados = associadoRepository.findComFiltros(bairro, escolaridade, idadeMin, idadeMax);

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
            @RequestParam(required = false) Integer idadeMin,
            @RequestParam(required = false) Integer idadeMax,
            HttpServletResponse response) throws IOException {

        List<Associado> associadosFiltrados = associadoRepository.findComFiltros(bairro, escolaridade, idadeMin, idadeMax);

        response.setContentType("application/octet-stream");
        String headerValue = "attachment; filename=relatorio_associados_" + getCurrentDateTime() + ".xlsx";
        response.setHeader("Content-Disposition", headerValue);

        relatorioService.gerarRelatorioExcelAssociados(response, associadosFiltrados);
    }

    private String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return LocalDateTime.now().format(formatter);
    }
}
