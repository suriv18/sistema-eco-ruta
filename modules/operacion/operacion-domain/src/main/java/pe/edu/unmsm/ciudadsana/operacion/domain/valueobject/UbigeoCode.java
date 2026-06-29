package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.Optional;
import java.util.regex.Pattern;

public record UbigeoCode(String value) implements ValueObject {
    private static final Pattern PATRON = Pattern.compile("^\\d{6}$");
    public UbigeoCode {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("UbigeoCode no puede ser nulo o vacío");
        if (!PATRON.matcher(value).matches()) throw new IllegalArgumentException("UbigeoCode debe tener exactamente 6 dígitos numéricos: " + value);
    }
    public static Optional<UbigeoCode> of(String value) {
        if (value == null || value.isBlank()) return Optional.empty();
        return Optional.of(new UbigeoCode(value));
    }
    @Override public String toString() { return value; }
}
