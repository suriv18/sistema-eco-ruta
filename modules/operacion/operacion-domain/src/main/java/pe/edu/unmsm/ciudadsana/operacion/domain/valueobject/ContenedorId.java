package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.UUID;

public record ContenedorId(UUID value) implements ValueObject {
    public ContenedorId {
        if (value == null) throw new IllegalArgumentException("ContenedorId no puede ser nulo");
    }
    public static ContenedorId of(UUID value) { return new ContenedorId(value); }
    public static ContenedorId of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("ContenedorId no puede ser nulo o vacío");
        try { return new ContenedorId(UUID.fromString(value)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("ContenedorId inválido: " + value); }
    }
    @Override public String toString() { return value.toString(); }
}
