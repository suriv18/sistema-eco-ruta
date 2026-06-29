package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

public record PasswordHash(String value) implements ValueObject {

    public PasswordHash {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El hash de contraseña no puede ser nulo o vacío");
        }
    }

    public static PasswordHash of(String value) {
        return new PasswordHash(value);
    }

    @Override
    public String toString() {
        return "[PROTECTED]";
    }
}
