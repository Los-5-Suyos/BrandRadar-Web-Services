package brandradar.reputationmonitoring.application.services;

import brandradar.reputationmonitoring.domain.model.aggregates.Mention;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

@Service
public class MentionExportService {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.of("America/Lima"));

    private static final String[] HEADERS = {
            "Fecha", "Plataforma", "Autor", "Contenido", "Sentimiento", "Likes", "Comentarios", "Estado"
    };

    public byte[] toCsv(List<Mention> mentions) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", HEADERS)).append("\n");
        for (Mention m : mentions) {
            sb.append(csvEscape(formatDate(m)))
                    .append(",").append(csvEscape(m.getSourcePlatform()))
                    .append(",").append(csvEscape(m.getAuthor()))
                    .append(",").append(csvEscape(m.getContent()))
                    .append(",").append(csvEscape(sentimentLabel(m)))
                    .append(",").append(m.getEngagementLikes())
                    .append(",").append(m.getEngagementComments())
                    .append(",").append(csvEscape(m.getStatus()))
                    .append("\n");
        }
        return sb.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

    public byte[] toExcel(List<Mention> mentions) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Menciones");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (Mention m : mentions) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(formatDate(m));
                row.createCell(1).setCellValue(nullSafe(m.getSourcePlatform()));
                row.createCell(2).setCellValue(nullSafe(m.getAuthor()));
                row.createCell(3).setCellValue(nullSafe(m.getContent()));
                row.createCell(4).setCellValue(sentimentLabel(m));
                row.createCell(5).setCellValue(m.getEngagementLikes());
                row.createCell(6).setCellValue(m.getEngagementComments());
                row.createCell(7).setCellValue(nullSafe(m.getStatus()));
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel export", e);
        }
    }

    public byte[] toPdf(List<Mention> mentions, String brandName) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            var font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            var fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            PDPageContentStream content = new PDPageContentStream(document, page);

            float y = 780;
            float margin = 40;
            float lineHeight = 14;

            content.beginText();
            content.setFont(fontBold, 16);
            content.newLineAtOffset(margin, y);
            content.showText("Reporte de Menciones - " + nullSafe(brandName));
            content.endText();
            y -= 30;

            content.beginText();
            content.setFont(font, 10);
            content.newLineAtOffset(margin, y);
            content.showText("Total de menciones: " + mentions.size());
            content.endText();
            y -= 25;

            for (Mention m : mentions) {
                if (y < 60) {
                    content.close();
                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    content = new PDPageContentStream(document, page);
                    y = 780;
                }

                String line1 = String.format("[%s] %s - %s (%s)",
                        formatDate(m), nullSafe(m.getSourcePlatform()), nullSafe(m.getAuthor()), sentimentLabel(m));
                String contentText = truncate(nullSafe(m.getContent()), 100);

                content.beginText();
                content.setFont(fontBold, 9);
                content.newLineAtOffset(margin, y);
                content.showText(line1);
                content.endText();
                y -= lineHeight;

                content.beginText();
                content.setFont(font, 9);
                content.newLineAtOffset(margin, y);
                content.showText(contentText);
                content.endText();
                y -= lineHeight + 6;
            }

            content.close();
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF export", e);
        }
    }

    private String formatDate(Mention m) {
        return m.getPublishedAt() != null ? DATE_FORMAT.format(m.getPublishedAt()) : "-";
    }

    private String sentimentLabel(Mention m) {
        if (m.getSentimentCompound() == null) return "NEUTRAL";
        double v = m.getSentimentCompound().doubleValue();
        if (v > 0.3) return "POSITIVO";
        if (v < -0.3) return "NEGATIVO";
        return "NEUTRAL";
    }

    private String nullSafe(String value) {
        return value != null ? value : "";
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        String escaped = value.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ");
        return "\"" + escaped + "\"";
    }
}