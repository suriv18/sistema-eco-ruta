package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.regex.Pattern;

public record CodigoZona(String value) implements ValueObject {
    private static final Pattern PATTERN = Pattern.compile("^[A-Z0-9_-]+$");
    public CodigoZona {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("CodigoZona no puede ser nulo o vacío");
        value = value.trim().toUpperCase();
        if (value.length() < 2 || value.length() > 50) throw new IllegalArgumentException("CodigoZona debe tener entre 2 y 50 caracteres");
        if (!PATTERN.matcher(value).matches()) throw new IllegalArgumentException("CodigoZona inválido: " + value);
    }
    public static CodigoZona of(String value) { return new CodigoZona(value); }
    @Override public String toString() { return value; }
}
