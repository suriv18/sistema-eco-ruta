package pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record CiudadanoId(UUID value) implements ValueObject {

    public CiudadanoId {
        if (value == null) {
            throw new IllegalArgumentException("CiudadanoId no puede ser nulo");
        }
    }

    public static CiudadanoId of(UUID value) {
        return new CiudadanoId(value);
    }

    public static CiudadanoId of(String value) {
        try {
            return new CiudadanoId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("CiudadanoId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
