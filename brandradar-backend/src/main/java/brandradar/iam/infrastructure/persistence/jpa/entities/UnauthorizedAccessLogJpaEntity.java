package brandradar.iam.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "UnauthorizedAccessLog")
public class UnauthorizedAccessLogJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UAL_id")
    private Long id;

    @Column(name = "USU_id")
    private Long userId;

    @Column(name = "UAL_resource", nullable = false, length = 500)
    private String resource;

    @Column(name = "UAL_ip_address", length = 45)
    private String ipAddress;

    @CreatedDate
    @Column(name = "UAL_occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;
}