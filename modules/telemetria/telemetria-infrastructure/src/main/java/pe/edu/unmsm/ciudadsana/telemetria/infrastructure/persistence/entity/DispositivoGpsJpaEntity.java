package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "dispositivo_gps", schema = "telemetria")
@Getter
@Setter
@NoArgsConstructor
public class DispositivoGpsJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "unidad_externo_id", nullable = false)
    private UUID unidadExternoId;

    @Column(name = "imei", length = 50)
    private String imei;

    @Column(name = "proveedor", length = 100)
    private String proveedor;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "ultimo_ping_en")
    private Instant ultimoPingEn;
}
