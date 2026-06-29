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
@Table(name = "evento_conectividad", schema = "telemetria")
@Getter
@Setter
@NoArgsConstructor
public class EventoConectividadJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "unidad_externo_id", nullable = false)
    private UUID unidadExternoId;

    @Column(name = "dispositivo_id", nullable = true)
    private UUID dispositivoId;

    @Column(name = "tipo_evento", nullable = false, length = 50)
    private String tipoEvento;

    @Column(name = "detalle", length = 500)
    private String detalle;

    @Column(name = "detectado_en", nullable = false)
    private Instant detectadoEn;
}
