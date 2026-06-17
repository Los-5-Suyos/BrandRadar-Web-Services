package brandradar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "brandradar")
@EnableScheduling
public class BrandradarBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrandradarBackendApplication.class, args);
    }
}