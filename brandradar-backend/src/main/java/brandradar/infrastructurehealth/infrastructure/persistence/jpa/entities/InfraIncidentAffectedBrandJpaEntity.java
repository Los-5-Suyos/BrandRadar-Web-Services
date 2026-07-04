package brandradar.infrastructurehealth.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "InfraIncidentAffectedBrand")
public class InfraIncidentAffectedBrandJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IAB_id")
    private Long id;

    @Column(name = "INI_id", nullable = false)
    private Long infraIncidentId;

    @Column(name = "BRA_id", nullable = false)
    private Long brandId;
}