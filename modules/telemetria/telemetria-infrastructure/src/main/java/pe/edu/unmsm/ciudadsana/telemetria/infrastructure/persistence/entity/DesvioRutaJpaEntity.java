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
@Table(name = "evento_desvio_ruta", schema = "telemetria")
@Getter
@Setter
@NoArgsConstructor
public class DesvioRutaJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "unidad_externo_id", nullable = false)
    private UUID unidadExternoId;

    @Column(name = "ruta_externo_id", nullable = false)
    private UUID rutaExternoId;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "distancia_desvio_m", nullable = false)
    private double distanciaDesvioM;

    @Column(name = "severidad", nullable = false, length = 30)
    private String severidad;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "detectado_en", nullable = false)
    private Instant detectadoEn;

    @Column(name = "atendido_en")
    private Instant atendidoEn;
}
