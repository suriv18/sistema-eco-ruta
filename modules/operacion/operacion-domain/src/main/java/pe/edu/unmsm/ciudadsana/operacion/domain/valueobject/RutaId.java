package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.UUID;

public record RutaId(UUID value) implements ValueObject {
    public RutaId {
        if (value == null) throw new IllegalArgumentException("RutaId no puede ser nulo");
    }
    public static RutaId of(UUID value) { return new RutaId(value); }
    public static RutaId of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("RutaId no puede ser nulo o vacío");
        try { return new RutaId(UUID.fromString(value)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("RutaId inválido: " + value); }
    }
    @Override public String toString() { return value.toString(); }
}
