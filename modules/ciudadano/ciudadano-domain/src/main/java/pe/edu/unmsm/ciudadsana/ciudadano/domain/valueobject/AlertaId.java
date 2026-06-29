package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record AlertaId(UUID value) implements ValueObject {

    public AlertaId {
        if (value == null) {
            throw new IllegalArgumentException("AlertaId no puede ser nulo");
        }
    }

    public static AlertaId of(UUID value) {
        return new AlertaId(value);
    }

    public static AlertaId of(String value) {
        try {
            return new AlertaId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("AlertaId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
