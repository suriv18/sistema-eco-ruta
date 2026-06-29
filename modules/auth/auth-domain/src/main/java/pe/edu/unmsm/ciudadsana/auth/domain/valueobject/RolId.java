package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record RolId(UUID value) implements ValueObject {

    public RolId {
        if (value == null) {
            throw new IllegalArgumentException("RolId no puede ser nulo");
        }
    }

    public static RolId of(UUID value) {
        return new RolId(value);
    }

    public static RolId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RolId no puede ser nulo o vacío");
        }
        try {
            return new RolId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("RolId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
