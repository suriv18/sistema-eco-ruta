package pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record UnidadExternoId(UUID value) implements ValueObject {

    public UnidadExternoId {
        if (value == null) {
            throw new IllegalArgumentException("UnidadExternoId no puede ser nulo");
        }
    }

    public static UnidadExternoId of(UUID value) {
        return new UnidadExternoId(value);
    }

    public static UnidadExternoId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UnidadExternoId no puede ser nulo o vacío");
        }
        try {
            return new UnidadExternoId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UnidadExternoId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
