package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

public record Email(String value) implements ValueObject {

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }
        value = value.trim().toLowerCase();
        if (!value.contains("@")) {
            throw new IllegalArgumentException("El email debe contener '@': " + value);
        }
        if (value.length() > 150) {
            throw new IllegalArgumentException("El email no puede superar 150 caracteres");
        }
    }

    public static Email of(String value) {
        return new Email(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
