package brandradar.iam.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Component
public class AvatarStorageService {

    private static final long MAX_SIZE_BYTES = 5L * 1024 * 1024; // 5 MB
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/png", "image/jpeg", "image/jpg", "image/webp");

    @Value("${app.uploads.dir:uploads}")
    private String uploadsDir;

    public String store(MultipartFile file, Long userId) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }
        if (file.getSize() > MAX_SIZE_BYTES) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File exceeds 5MB limit");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Unsupported file type: " + contentType + ". Allowed: png, jpeg, webp");
        }

        try {
            String extension = extractExtension(file.getOriginalFilename(), contentType);
            String filename = "user-" + userId + "-" + UUID.randomUUID() + extension;

            Path targetDir = Paths.get(uploadsDir, "avatars");
            Files.createDirectories(targetDir);
            Path targetPath = targetDir.resolve(filename);
            file.transferTo(targetPath);

            return "/uploads/avatars/" + filename;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not save file: " + e.getMessage());
        }
    }

    private String extractExtension(String originalFilename, String contentType) {
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf('.'));
        }
        return switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg", "image/jpg" -> ".jpg";
            case "image/webp" -> ".webp";
            default -> "";
        };
    }
}