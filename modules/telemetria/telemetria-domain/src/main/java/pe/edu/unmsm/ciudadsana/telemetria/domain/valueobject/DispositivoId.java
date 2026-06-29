package pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record DispositivoId(UUID value) implements ValueObject {

    public DispositivoId {
        if (value == null) {
            throw new IllegalArgumentException("DispositivoId no puede ser nulo");
        }
    }

    public static DispositivoId of(UUID value) {
        return new DispositivoId(value);
    }

    public static DispositivoId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DispositivoId no puede ser nulo o vacío");
        }
        try {
            return new DispositivoId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("DispositivoId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
