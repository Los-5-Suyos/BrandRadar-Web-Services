package brandradar.shared.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ErrorResource(
        String timestamp,
        int status,
        String code,
        String message
) {
    public static ErrorResource of(int status, String code, String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return new ErrorResource(timestamp, status, code, message);
    }
}
