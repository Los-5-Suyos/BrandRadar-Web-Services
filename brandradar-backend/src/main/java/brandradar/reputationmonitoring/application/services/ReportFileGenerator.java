package brandradar.reputationmonitoring.application.services;

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
import java.nio.charset.StandardCharsets;

@Service
public class ReportFileGenerator {

    public byte[] toCsv(ReportData data) {
        StringBuilder sb = new StringBuilder();
        sb.append("Reporte de Reputacion - ").append(data.brandName()).append("\n");
        sb.append("Periodo: ").append(data.periodFrom()).append(" a ").append(data.periodTo()).append("\n\n");

        sb.append("RESUMEN EJECUTIVO\n");
        sb.append(csvEscape(data.executiveSummaryText())).append("\n\n");

        sb.append("METRICA,VALOR,VARIACION,QUE SIGNIFICA\n");
        sb.append("Sentiment Score,").append(data.sentimentScore()).append(",")
                .append(fmtDelta(data.sentimentScoreDeltaPercent())).append(",")
                .append(csvEscape(data.sentimentScoreExplanation())).append("\n");
        sb.append("Menciones totales,").append(data.totalMentions()).append(",")
                .append(fmtDelta(data.mentionsDeltaPercent())).append(",")
                .append(csvEscape(data.mentionsExplanation())).append("\n");
        sb.append("Reach estimado,").append(data.reachEstimate()).append(",")
                .append(fmtDelta(data.reachDeltaPercent())).append(",")
                .append(csvEscape(data.reachExplanation())).append("\n\n");

        sb.append("EVOLUCION DE SENTIMIENTO POR DIA\n");
        sb.append("Fecha,Score\n");
        for (var point : data.sentimentEvolution()) {
            sb.append(point.date()).append(",").append(point.sentimentScore()).append("\n");
        }
        sb.append("\n");

        sb.append("TOP KEYWORDS\n");
        sb.append("Keyword,Menciones,Score (0-5)\n");
        for (var kw : data.topKeywords()) {
            sb.append(csvEscape(kw.keyword())).append(",").append(kw.count()).append(",").append(kw.score()).append("\n");
        }
        sb.append("\n");

        sb.append("CUENTAS CRITICAS\n");
        sb.append("Autor,Plataforma,Menciones,Sentimiento promedio\n");
        for (var acc : data.criticalAccounts()) {
            sb.append(csvEscape(acc.author())).append(",").append(csvEscape(acc.platform())).append(",")
                    .append(acc.mentionsCount()).append(",").append(acc.avgSentiment()).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] toExcel(ReportData data) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            Sheet summarySheet = workbook.createSheet("Resumen");
            int r = 0;
            r = writeRow(summarySheet, r, boldStyle, "Reporte de Reputación - " + data.brandName());
            r = writeRow(summarySheet, r, null, "Periodo: " + data.periodFrom() + " a " + data.periodTo());
            r++;
            r = writeRow(summarySheet, r, boldStyle, "Resumen Ejecutivo");
            r = writeRow(summarySheet, r, null, data.executiveSummaryText());
            r++;
            r = writeRow(summarySheet, r, boldStyle, "Métrica", "Valor", "Variación", "Qué significa");
            r = writeRow(summarySheet, r, null, "Sentiment Score", String.valueOf(data.sentimentScore()),
                    fmtDelta(data.sentimentScoreDeltaPercent()), data.sentimentScoreExplanation());
            r = writeRow(summarySheet, r, null, "Menciones totales", String.valueOf(data.totalMentions()),
                    fmtDelta(data.mentionsDeltaPercent()), data.mentionsExplanation());
            r = writeRow(summarySheet, r, null, "Reach estimado", String.valueOf(data.reachEstimate()),
                    fmtDelta(data.reachDeltaPercent()), data.reachExplanation());
            for (int i = 0; i < 4; i++) summarySheet.autoSizeColumn(i);

            Sheet evolutionSheet = workbook.createSheet("Evolucion");
            int er = writeRow(evolutionSheet, 0, boldStyle, "Fecha", "Score");
            for (var point : data.sentimentEvolution()) {
                er = writeRow(evolutionSheet, er, null, point.date().toString(), String.valueOf(point.sentimentScore()));
            }

            Sheet keywordsSheet = workbook.createSheet("Top Keywords");
            int kr = writeRow(keywordsSheet, 0, boldStyle, "Keyword", "Menciones", "Score (0-5)");
            for (var kw : data.topKeywords()) {
                kr = writeRow(keywordsSheet, kr, null, kw.keyword(), String.valueOf(kw.count()), String.valueOf(kw.score()));
            }

            Sheet accountsSheet = workbook.createSheet("Cuentas Criticas");
            int ar = writeRow(accountsSheet, 0, boldStyle, "Autor", "Plataforma", "Menciones", "Sentimiento promedio");
            for (var acc : data.criticalAccounts()) {
                ar = writeRow(accountsSheet, ar, null, acc.author(), acc.platform(),
                        String.valueOf(acc.mentionsCount()), String.valueOf(acc.avgSentiment()));
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel report", e);
        }
    }

    public byte[] toPdf(ReportData data) {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            var font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            var fontBold = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            PdfCursor cursor = new PdfCursor(document, font, fontBold);

            cursor.title("Reporte de Reputación - " + data.brandName());
            cursor.text("Periodo: " + data.periodFrom() + " a " + data.periodTo());
            cursor.gap();

            cursor.subtitle("Resumen Ejecutivo");
            cursor.wrappedText(data.executiveSummaryText());
            cursor.gap();

            cursor.subtitle("Métricas del periodo");
            cursor.text("Sentiment Score: " + data.sentimentScore() + " " + fmtDelta(data.sentimentScoreDeltaPercent()));
            cursor.smallText(data.sentimentScoreExplanation());
            cursor.text("Menciones totales: " + data.totalMentions() + " " + fmtDelta(data.mentionsDeltaPercent()));
            cursor.smallText(data.mentionsExplanation());
            cursor.text("Reach estimado: " + data.reachEstimate() + " " + fmtDelta(data.reachDeltaPercent()));
            cursor.smallText(data.reachExplanation());
            cursor.gap();

            cursor.subtitle("Top Keywords");
            for (var kw : data.topKeywords()) {
                cursor.text("• " + kw.keyword() + " — " + kw.count() + " menciones, score " + kw.score() + "/5");
            }
            cursor.gap();

            cursor.subtitle("Cuentas críticas");
            for (var acc : data.criticalAccounts()) {
                cursor.text("• " + acc.author() + " (" + acc.platform() + ") — "
                        + acc.mentionsCount() + " menciones, sentimiento " + acc.avgSentiment());
            }

            cursor.close();
            document.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }

    private int writeRow(Sheet sheet, int rowIdx, CellStyle style, String... values) {
        Row row = sheet.createRow(rowIdx);
        for (int i = 0; i < values.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(values[i]);
            if (style != null) cell.setCellStyle(style);
        }
        return rowIdx + 1;
    }

    private String fmtDelta(Double delta) {
        if (delta == null) return "(sin dato del periodo anterior)";
        return (delta >= 0 ? "+" : "") + delta + "% vs. periodo anterior";
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"").replace("\n", " ") + "\"";
    }

    /** Ayudante interno para no repetir el manejo de posición Y / salto de página en el PDF. */
    private static class PdfCursor {
        private final PDDocument document;
        private final PDType1Font font;
        private final PDType1Font fontBold;
        private PDPage page;
        private PDPageContentStream content;
        private float y;
        private static final float MARGIN = 40;

        PdfCursor(PDDocument document, PDType1Font font, PDType1Font fontBold) throws IOException {
            this.document = document;
            this.font = font;
            this.fontBold = fontBold;
            newPage();
        }

        private void newPage() throws IOException {
            page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            content = new PDPageContentStream(document, page);
            y = 780;
        }

        private void ensureSpace() throws IOException {
            if (y < 60) {
                content.close();
                newPage();
            }
        }

        void title(String text) throws IOException {
            ensureSpace();
            content.beginText();
            content.setFont(fontBold, 16);
            content.newLineAtOffset(MARGIN, y);
            content.showText(text);
            content.endText();
            y -= 24;
        }

        void subtitle(String text) throws IOException {
            ensureSpace();
            content.beginText();
            content.setFont(fontBold, 12);
            content.newLineAtOffset(MARGIN, y);
            content.showText(text);
            content.endText();
            y -= 18;
        }

        void text(String text) throws IOException {
            ensureSpace();
            content.beginText();
            content.setFont(font, 10);
            content.newLineAtOffset(MARGIN, y);
            content.showText(truncate(text, 100));
            content.endText();
            y -= 14;
        }

        void smallText(String text) throws IOException {
            ensureSpace();
            content.beginText();
            content.setFont(font, 8);
            content.newLineAtOffset(MARGIN + 10, y);
            content.showText(truncate(text, 130));
            content.endText();
            y -= 16;
        }

        void wrappedText(String text) throws IOException {
            for (String line : wrap(text, 100)) {
                text(line);
            }
        }

        void gap() {
            y -= 10;
        }

        void close() throws IOException {
            content.close();
        }

        private String truncate(String text, int max) {
            if (text == null) return "";
            return text.length() <= max ? text : text.substring(0, max) + "...";
        }

        private java.util.List<String> wrap(String text, int maxLineLength) {
            java.util.List<String> lines = new java.util.ArrayList<>();
            if (text == null) return lines;
            String[] words = text.split(" ");
            StringBuilder current = new StringBuilder();
            for (String word : words) {
                if (current.length() + word.length() + 1 > maxLineLength) {
                    lines.add(current.toString());
                    current = new StringBuilder();
                }
                current.append(word).append(" ");
            }
            if (!current.isEmpty()) lines.add(current.toString());
            return lines;
        }
    }
}