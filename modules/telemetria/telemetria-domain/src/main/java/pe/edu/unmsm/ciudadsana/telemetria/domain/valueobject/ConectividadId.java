package pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record ConectividadId(UUID value) implements ValueObject {

    public ConectividadId {
        if (value == null) {
            throw new IllegalArgumentException("ConectividadId no puede ser nulo");
        }
    }

    public static ConectividadId of(UUID value) {
        return new ConectividadId(value);
    }

    public static ConectividadId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ConectividadId no puede ser nulo o vacío");
        }
        try {
            return new ConectividadId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ConectividadId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
