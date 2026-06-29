package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record RefreshTokenId(UUID value) implements ValueObject {

    public RefreshTokenId {
        if (value == null) {
            throw new IllegalArgumentException("RefreshTokenId no puede ser nulo");
        }
    }

    public static RefreshTokenId of(UUID value) {
        return new RefreshTokenId(value);
    }

    public static RefreshTokenId of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("RefreshTokenId no puede ser nulo o vacío");
        }
        try {
            return new RefreshTokenId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("RefreshTokenId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
