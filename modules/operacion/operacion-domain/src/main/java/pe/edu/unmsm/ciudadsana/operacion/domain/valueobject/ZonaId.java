package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.UUID;

public record ZonaId(UUID value) implements ValueObject {
    public ZonaId {
        if (value == null) throw new IllegalArgumentException("ZonaId no puede ser nulo");
    }
    public static ZonaId of(UUID value) { return new ZonaId(value); }
    public static ZonaId of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("ZonaId no puede ser nulo o vacío");
        try { return new ZonaId(UUID.fromString(value)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("ZonaId inválido: " + value); }
    }
    @Override public String toString() { return value.toString(); }
}
