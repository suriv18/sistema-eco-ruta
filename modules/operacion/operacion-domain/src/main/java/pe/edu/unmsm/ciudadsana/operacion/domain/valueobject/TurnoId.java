package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.UUID;

public record TurnoId(UUID value) implements ValueObject {
    public TurnoId {
        if (value == null) throw new IllegalArgumentException("TurnoId no puede ser nulo");
    }
    public static TurnoId of(UUID value) { return new TurnoId(value); }
    public static TurnoId of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("TurnoId no puede ser nulo o vacío");
        try { return new TurnoId(UUID.fromString(value)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("TurnoId inválido: " + value); }
    }
    @Override public String toString() { return value.toString(); }
}
