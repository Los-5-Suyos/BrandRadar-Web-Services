package brandradar.reputationmonitoring.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class ReportStorageService {

    @Value("${app.uploads.dir:uploads}")
    private String uploadsDir;

    public record StoredFile(String url, long sizeBytes) {}

    public StoredFile store(byte[] content, Long workspaceId, String format) {
        try {
            String extension = switch (format.toLowerCase()) {
                case "excel", "xlsx" -> ".xlsx";
                case "pdf" -> ".pdf";
                default -> ".csv";
            };
            String filename = "report-" + workspaceId + "-" + UUID.randomUUID() + extension;

            Path targetDir = Paths.get(uploadsDir, "reports");
            Files.createDirectories(targetDir);
            Path targetPath = targetDir.resolve(filename);
            Files.write(targetPath, content);

            return new StoredFile("/uploads/reports/" + filename, content.length);
        } catch (IOException e) {
            throw new RuntimeException("Could not save report file: " + e.getMessage(), e);
        }
    }
}