package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record ZonaExternoId(UUID value) implements ValueObject {

    public ZonaExternoId {
        if (value == null) {
            throw new IllegalArgumentException("ZonaExternoId no puede ser nulo");
        }
    }

    public static ZonaExternoId of(UUID value) {
        return new ZonaExternoId(value);
    }

    public static ZonaExternoId of(String value) {
        try {
            return new ZonaExternoId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ZonaExternoId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
