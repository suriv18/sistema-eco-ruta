package pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_event", schema = "auditoria")
@Getter
@Setter
@NoArgsConstructor
public class OutboxEventJpaEntity {

    @Id
    @Column(name = "outbox_id")
    private UUID id;

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

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    @Column(name = "publicado_en")
    private Instant publicadoEn;

    @Column(name = "error_mensaje", columnDefinition = "text")
    private String errorMensaje;
}
