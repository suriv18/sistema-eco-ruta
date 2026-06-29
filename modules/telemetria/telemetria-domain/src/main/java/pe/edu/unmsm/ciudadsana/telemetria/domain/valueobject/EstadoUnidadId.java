package pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record EstadoUnidadId(UUID value) implements ValueObject {

    public EstadoUnidadId {
        if (value == null) {
            throw new IllegalArgumentException("EstadoUnidadId no puede ser nulo");
        }
    }

    public static EstadoUnidadId of(UUID value) {
        return new EstadoUnidadId(value);
    }

    public static EstadoUnidadId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("EstadoUnidadId no puede ser nulo o vacío");
        }
        try {
            return new EstadoUnidadId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("EstadoUnidadId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
