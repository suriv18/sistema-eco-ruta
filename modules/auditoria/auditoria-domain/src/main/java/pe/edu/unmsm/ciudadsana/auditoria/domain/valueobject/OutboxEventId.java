package pe.edu.unmsm.ciudadsana.auditoria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record OutboxEventId(UUID value) implements ValueObject {

    public OutboxEventId {
        if (value == null) throw new IllegalArgumentException("OutboxEventId no puede ser nulo");
    }

    public static OutboxEventId of(UUID value) { return new OutboxEventId(value); }
    public static OutboxEventId generate() { return new OutboxEventId(UUID.randomUUID()); }
}
