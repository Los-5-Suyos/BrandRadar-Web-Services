package brandradar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "brandradar")
public class BrandradarBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrandradarBackendApplication.class, args);
    }
}