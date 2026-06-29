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

/**
 * JPA entity for the estado_unidad_actual table.
 * Extends TenantAwareJpaEntity so the PK (id) is a surrogate UUID and
 * tenantId is inherited. The business key is unidadExternoId + tenantId.
 */
@Entity
@Table(name = "estado_unidad_actual", schema = "telemetria")
@Getter
@Setter
@NoArgsConstructor
public class EstadoUnidadJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "unidad_externo_id", nullable = false)
    private UUID unidadExternoId;

    @Column(name = "ruta_externo_id")
    private UUID rutaExternoId;

    @Column(name = "latitud", nullable = false)
    private double latitud;

    @Column(name = "longitud", nullable = false)
    private double longitud;

    @Column(name = "ultima_velocidad_kmh")
    private Double ultimaVelocidadKmh;

    @Column(name = "ultimo_ping_en")
    private Instant ultimoPingEn;

    @Column(name = "estado_movimiento", nullable = false, length = 30)
    private String estadoMovimiento;
}
