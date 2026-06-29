package pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record PingId(UUID value) implements ValueObject {

    public PingId {
        if (value == null) {
            throw new IllegalArgumentException("PingId no puede ser nulo");
        }
    }

    public static PingId of(UUID value) {
        return new PingId(value);
    }

    public static PingId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PingId no puede ser nulo o vacío");
        }
        try {
            return new PingId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("PingId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
