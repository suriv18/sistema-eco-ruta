package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.UUID;

public record UnidadId(UUID value) implements ValueObject {
    public UnidadId {
        if (value == null) throw new IllegalArgumentException("UnidadId no puede ser nulo");
    }
    public static UnidadId of(UUID value) { return new UnidadId(value); }
    public static UnidadId of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("UnidadId no puede ser nulo o vacío");
        try { return new UnidadId(UUID.fromString(value)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("UnidadId inválido: " + value); }
    }
    @Override public String toString() { return value.toString(); }
}
