package pe.edu.unmsm.ciudadsana.auth.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.regex.Pattern;

public record Username(String value) implements ValueObject {

    private static final Pattern ALLOWED = Pattern.compile("^[a-z0-9_-]+$");

    public Username {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El username no puede ser nulo o vacío");
        }
        value = value.toLowerCase();
        if (value.length() < 3 || value.length() > 80) {
            throw new IllegalArgumentException("El username debe tener entre 3 y 80 caracteres");
        }
        if (!ALLOWED.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "El username solo puede contener letras, dígitos, guiones y guiones bajos: " + value);
        }
    }

    public static Username of(String value) {
        return new Username(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
