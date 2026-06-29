package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record PermisoId(UUID value) implements ValueObject {

    public PermisoId {
        if (value == null) {
            throw new IllegalArgumentException("PermisoId no puede ser nulo");
        }
    }

    public static PermisoId of(UUID value) {
        return new PermisoId(value);
    }

    public static PermisoId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PermisoId no puede ser nulo o vacío");
        }
        try {
            return new PermisoId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("PermisoId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
