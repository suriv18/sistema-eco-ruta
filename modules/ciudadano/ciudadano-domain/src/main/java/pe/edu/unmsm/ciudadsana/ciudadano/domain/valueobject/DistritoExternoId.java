package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record DistritoExternoId(UUID value) implements ValueObject {

    public DistritoExternoId {
        if (value == null) {
            throw new IllegalArgumentException("DistritoExternoId no puede ser nulo");
        }
    }

    public static DistritoExternoId of(UUID value) {
        return new DistritoExternoId(value);
    }

    public static DistritoExternoId of(String value) {
        try {
            return new DistritoExternoId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("DistritoExternoId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
