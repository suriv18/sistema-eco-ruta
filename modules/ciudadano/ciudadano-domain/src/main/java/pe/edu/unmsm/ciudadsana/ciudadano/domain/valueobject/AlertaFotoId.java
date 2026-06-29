package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record AlertaFotoId(UUID value) implements ValueObject {

    public AlertaFotoId {
        if (value == null) {
            throw new IllegalArgumentException("AlertaFotoId no puede ser nulo");
        }
    }

    public static AlertaFotoId of(UUID value) {
        return new AlertaFotoId(value);
    }

    public static AlertaFotoId of(String value) {
        try {
            return new AlertaFotoId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("AlertaFotoId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
