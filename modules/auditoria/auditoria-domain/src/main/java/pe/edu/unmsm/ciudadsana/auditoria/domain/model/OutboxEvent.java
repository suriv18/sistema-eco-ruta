package pe.edu.unmsm.ciudadsana.auditoria.domain.model;

import pe.edu.unmsm.ciudadsana.auditoria.domain.enums.EstadoOutbox;
import pe.edu.unmsm.ciudadsana.auditoria.domain.valueobject.OutboxEventId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class OutboxEvent {

    private final OutboxEventId id;
    private final TenantId tenantId;
    private final String aggregateType;
    private final UUID aggregateId;
    private final String eventType;
    private final String payload;
    private EstadoOutbox estado;
    private final Instant creadoEn;
    private Optional<Instant> publicadoEn;
    private Optional<String> errorMensaje;

    private OutboxEvent(
            OutboxEventId id,
            TenantId tenantId,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload,
            EstadoOutbox estado,
            Instant creadoEn,
            Optional<Instant> publicadoEn,
            Optional<String> errorMensaje
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.estado = estado;
        this.creadoEn = creadoEn;
        this.publicadoEn = publicadoEn;
        this.errorMensaje = errorMensaje;
    }

    public static OutboxEvent create(
            OutboxEventId id,
            TenantId tenantId,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload
    ) {
        if (id == null) throw new IllegalArgumentException("OutboxEvent: id no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("OutboxEvent: tenantId no puede ser nulo");
        if (aggregateType == null || aggregateType.isBlank()) throw new IllegalArgumentException("OutboxEvent: aggregateType no puede ser vacío");
        if (eventType == null || eventType.isBlank()) throw new IllegalArgumentException("OutboxEvent: eventType no puede ser vacío");
        if (payload == null || payload.isBlank()) throw new IllegalArgumentException("OutboxEvent: payload no puede ser vacío");

        return new OutboxEvent(
                id, tenantId, aggregateType, aggregateId, eventType, payload,
                EstadoOutbox.PENDIENTE, Instant.now(), Optional.empty(), Optional.empty()
        );
    }

    public static OutboxEvent reconstitute(
            OutboxEventId id,
            TenantId tenantId,
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload,
            EstadoOutbox estado,
            Instant creadoEn,
            Instant publicadoEn,
            String errorMensaje
    ) {
        return new OutboxEvent(
                id, tenantId, aggregateType, aggregateId, eventType, payload,
                estado, creadoEn,
                Optional.ofNullable(publicadoEn),
                Optional.ofNullable(errorMensaje)
        );
    }

    public void marcarPublicado(Instant ahora) {
        if (this.estado != EstadoOutbox.PENDIENTE) {
            throw new IllegalStateException("Solo se puede publicar un outbox en estado PENDIENTE");
        }
        this.estado = EstadoOutbox.PUBLICADO;
        this.publicadoEn = Optional.of(ahora);
        this.errorMensaje = Optional.empty();
    }

    public void marcarError(String mensaje) {
        if (this.estado != EstadoOutbox.PENDIENTE) {
            throw new IllegalStateException("Solo se puede marcar error en un outbox en estado PENDIENTE");
        }
        this.estado = EstadoOutbox.ERROR;
        this.errorMensaje = Optional.ofNullable(mensaje);
    }

    public OutboxEventId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getAggregateType() { return aggregateType; }
    public UUID getAggregateId() { return aggregateId; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public EstadoOutbox getEstado() { return estado; }
    public Instant getCreadoEn() { return creadoEn; }
    public Optional<Instant> getPublicadoEn() { return publicadoEn; }
    public Optional<String> getErrorMensaje() { return errorMensaje; }
}
