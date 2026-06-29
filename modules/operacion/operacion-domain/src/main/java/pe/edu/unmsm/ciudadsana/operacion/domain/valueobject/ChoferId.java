package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.UUID;

public record ChoferId(UUID value) implements ValueObject {
    public ChoferId {
        if (value == null) throw new IllegalArgumentException("ChoferId no puede ser nulo");
    }
    public static ChoferId of(UUID value) { return new ChoferId(value); }
    public static ChoferId of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("ChoferId no puede ser nulo o vacío");
        try { return new ChoferId(UUID.fromString(value)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("ChoferId inválido: " + value); }
    }
    @Override public String toString() { return value.toString(); }
}
