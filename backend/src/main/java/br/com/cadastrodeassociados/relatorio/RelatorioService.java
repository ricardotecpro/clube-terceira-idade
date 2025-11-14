package br.com.cadastrodeassociados.relatorio;

import br.com.cadastrodeassociados.associado.Associado;
import com.lowagie.text.Font;
import com.lowagie.text.*;
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
import com.lowagie.text.DocumentException;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

@Service
public class RelatorioService {

    private static final int TOTAL_COLUNAS_PDF_EXCEL = 9;

    public void gerarRelatorioPdfAssociados(HttpServletResponse response, List<Associado> associados) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Relatório de Associados", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(TOTAL_COLUNAS_PDF_EXCEL);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{0.4f, 2f, 1.5f, 2f, 1.2f, 0.8f, 1.2f, 1.5f, 2f});

        addTableHeaderPdf(table);

        for (Associado associado : associados) {
            addRowsPdf(table, associado);
        }

        document.add(table);
        document.close();
    }

    private void addTableHeaderPdf(PdfPTable table) throws DocumentException {
        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontHeader.setColor(Color.WHITE);

        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(new Color(0, 123, 255));
        headerCell.setPadding(5);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        String[] headers = {"ID", "Nome", "CPF", "Endereço", "Cidade", "Estado", "Telefone", "Escolaridade", "Email"};

        for (String header : headers) {
            headerCell.setPhrase(new Phrase(header, fontHeader));
            table.addCell(headerCell);
        }
    }

    private void addRowsPdf(PdfPTable table, Associado associado) throws DocumentException {
        String cidadeNome = associado.getCidade() != null ? associado.getCidade().getNome() : "";
        String estadoNome = associado.getCidade() != null ? associado.getCidade().getEstado() : "";

        table.addCell(String.valueOf(associado.getId()));
        table.addCell(associado.getNome());
        table.addCell(associado.getCpf());
        table.addCell(associado.getEndereco());
        table.addCell(cidadeNome);
        table.addCell(estadoNome);
        table.addCell(associado.getTelefone());
        table.addCell(associado.getEscolaridade());
        table.addCell(associado.getEmail());
    }

    public void gerarRelatorioExcelAssociados(HttpServletResponse response, List<Associado> associados) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Associados");

        Row headerRow = sheet.createRow(0);

        String[] columns = {"ID", "Nome", "CPF", "Endereço", "Cidade", "Estado", "Telefone", "Escolaridade", "Email"};

        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (Associado associado : associados) {
            Row row = sheet.createRow(rowNum++);

            String cidadeNome = associado.getCidade() != null ? associado.getCidade().getNome() : "";
            String estadoNome = associado.getCidade() != null ? associado.getCidade().getEstado() : "";

            row.createCell(0).setCellValue(associado.getId());
            row.createCell(1).setCellValue(associado.getNome());
            row.createCell(2).setCellValue(associado.getCpf());
            row.createCell(3).setCellValue(associado.getEndereco());
            row.createCell(4).setCellValue(cidadeNome);
            row.createCell(5).setCellValue(estadoNome);
            row.createCell(6).setCellValue(associado.getTelefone());
            row.createCell(7).setCellValue(associado.getEscolaridade());
            row.createCell(8).setCellValue(associado.getEmail());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
