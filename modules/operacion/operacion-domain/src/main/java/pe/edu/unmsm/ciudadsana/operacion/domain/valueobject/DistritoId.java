package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.UUID;

public record DistritoId(UUID value) implements ValueObject {
    public DistritoId {
        if (value == null) throw new IllegalArgumentException("DistritoId no puede ser nulo");
    }
    public static DistritoId of(UUID value) { return new DistritoId(value); }
    public static DistritoId of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("DistritoId no puede ser nulo o vacío");
        try { return new DistritoId(UUID.fromString(value)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("DistritoId inválido: " + value); }
    }
    @Override public String toString() { return value.toString(); }
}
