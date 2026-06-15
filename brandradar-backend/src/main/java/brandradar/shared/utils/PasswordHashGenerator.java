package brandradar.shared.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Pro2026!: " + encoder.encode("Pro2026!"));
        System.out.println("Enterprise2026!: " + encoder.encode("Enterprise2026!"));
    }
}