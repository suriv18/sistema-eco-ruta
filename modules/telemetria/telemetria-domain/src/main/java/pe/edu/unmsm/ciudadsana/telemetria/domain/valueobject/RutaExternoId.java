package pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record RutaExternoId(UUID value) implements ValueObject {

    public RutaExternoId {
        if (value == null) {
            throw new IllegalArgumentException("RutaExternoId no puede ser nulo");
        }
    }

    public static RutaExternoId of(UUID value) {
        return new RutaExternoId(value);
    }

    public static RutaExternoId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RutaExternoId no puede ser nulo o vacío");
        }
        try {
            return new RutaExternoId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("RutaExternoId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
