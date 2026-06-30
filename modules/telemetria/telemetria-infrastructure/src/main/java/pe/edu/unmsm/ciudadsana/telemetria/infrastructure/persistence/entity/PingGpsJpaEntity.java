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
@Table(name = "telemetria_gps", schema = "telemetria")
@Getter
@Setter
@NoArgsConstructor
public class PingGpsJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "dispositivo_id", nullable = false)
    private UUID dispositivoId;

    @Column(name = "unidad_externo_id", nullable = false)
    private UUID unidadExternoId;

    @Column(name = "ruta_externo_id")
    private UUID rutaExternoId;

    @Column(name = "ts", nullable = false)
    private Instant ts;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "velocidad_kmh")
    private Double velocidadKmh;

    @Column(name = "rumbo_grados")
    private Double rumboGrados;

    @Column(name = "precision_m")
    private Double precisionM;

    @Column(name = "origen", nullable = false, length = 30)
    private String origen;

    @Column(name = "recibido_en", nullable = false)
    private Instant recibidoEn;
}
