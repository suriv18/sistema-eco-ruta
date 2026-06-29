package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record ValidacionId(UUID value) implements ValueObject {

    public ValidacionId {
        if (value == null) {
            throw new IllegalArgumentException("ValidacionId no puede ser nulo");
        }
    }

    public static ValidacionId of(UUID value) {
        return new ValidacionId(value);
    }

    public static ValidacionId of(String value) {
        try {
            return new ValidacionId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ValidacionId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
