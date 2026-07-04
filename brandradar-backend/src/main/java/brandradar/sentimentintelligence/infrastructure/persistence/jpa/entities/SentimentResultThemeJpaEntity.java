package brandradar.sentimentintelligence.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "SentimentResultTheme")
public class SentimentResultThemeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SRT_id")
    private Long id;

    @Column(name = "SER_id", nullable = false)
    private Long sentimentResultId;

    @Column(name = "SRT_theme", nullable = false, length = 255)
    private String theme;
}