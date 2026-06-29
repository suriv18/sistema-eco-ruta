package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record UsuarioId(UUID value) implements ValueObject {

    public UsuarioId {
        if (value == null) {
            throw new IllegalArgumentException("UsuarioId no puede ser nulo");
        }
    }

    public static UsuarioId of(UUID value) {
        return new UsuarioId(value);
    }

    public static UsuarioId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UsuarioId no puede ser nulo o vacío");
        }
        try {
            return new UsuarioId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UsuarioId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
