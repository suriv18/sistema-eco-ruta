package pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.BaseJpaEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_event", schema = "auditoria")
@Getter
@Setter
@NoArgsConstructor
public class OutboxEventJpaEntity extends BaseJpaEntity {

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    private String payload;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "publicado_en")
    private Instant publicadoEn;

    @Column(name = "error_mensaje", columnDefinition = "text")
    private String errorMensaje;
}
