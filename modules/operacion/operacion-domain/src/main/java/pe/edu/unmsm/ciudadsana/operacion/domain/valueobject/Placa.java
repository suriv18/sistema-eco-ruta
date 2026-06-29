package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.regex.Pattern;

public record Placa(String value) implements ValueObject {
    private static final Pattern PATRON_ANTIGUO = Pattern.compile("^[A-Z]{3}-\\d{3}$");
    private static final Pattern PATRON_NUEVO = Pattern.compile("^[A-Z]\\d{4}[A-Z]$");
    public Placa {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Placa no puede ser nula o vacía");
        value = value.trim().toUpperCase();
        if (!PATRON_ANTIGUO.matcher(value).matches() && !PATRON_NUEVO.matcher(value).matches())
            throw new IllegalArgumentException("Placa con formato peruano inválido: " + value);
    }
    public static Placa of(String value) { return new Placa(value); }
    @Override public String toString() { return value; }
}
