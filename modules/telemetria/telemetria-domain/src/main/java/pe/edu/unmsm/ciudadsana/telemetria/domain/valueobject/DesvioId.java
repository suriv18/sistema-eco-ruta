package pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record DesvioId(UUID value) implements ValueObject {

    public DesvioId {
        if (value == null) {
            throw new IllegalArgumentException("DesvioId no puede ser nulo");
        }
    }

    public static DesvioId of(UUID value) {
        return new DesvioId(value);
    }

    public static DesvioId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("DesvioId no puede ser nulo o vacío");
        }
        try {
            return new DesvioId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("DesvioId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
