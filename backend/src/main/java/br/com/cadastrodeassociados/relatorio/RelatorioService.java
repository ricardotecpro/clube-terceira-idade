package br.com.cadastrodeassociados.relatorio;

import br.com.cadastrodeassociados.associado.Associado;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Service
public class RelatorioService {

    public void gerarRelatorioPdfAssociados(HttpServletResponse response, List<Associado> associados) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Título
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
        Paragraph title = new Paragraph("Relatório de Associados", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" ")); // Linha em branco

        // Tabela
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3f, 2f, 2f, 2f});

        // Cabeçalho da Tabela
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        PdfPCell hcell;
        hcell = new PdfPCell(new Phrase("Nome", headFont));
        hcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("CPF", headFont));
        hcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Telefone", headFont));
        hcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Situação", headFont));
        hcell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(hcell);

        // Dados da Tabela
        for (Associado associado : associados) {
            PdfPCell cell;

            cell = new PdfPCell(new Phrase(associado.getNome()));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(associado.getCpf()));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(associado.getTelefone()));
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(associado.getSituacao()));
            table.addCell(cell);
        }

        document.add(table);
        document.close();
    }

    public void gerarRelatorioExcelAssociados(HttpServletResponse response, List<Associado> associados) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Associados");

        // Cabeçalho
        String[] headers = {"Nome", "CPF", "Telefone", "Situação", "Endereço", "Escolaridade"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Dados
        int rowNum = 1;
        for (Associado associado : associados) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(associado.getNome());
            row.createCell(1).setCellValue(associado.getCpf());
            row.createCell(2).setCellValue(associado.getTelefone());
            row.createCell(3).setCellValue(associado.getSituacao());
            row.createCell(4).setCellValue(associado.getEndereco());
            row.createCell(5).setCellValue(associado.getEscolaridade());
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
