package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.UUID;

public record HorarioRecoleccionId(UUID value) implements ValueObject {
    public HorarioRecoleccionId {
        if (value == null) throw new IllegalArgumentException("HorarioRecoleccionId no puede ser nulo");
    }
    public static HorarioRecoleccionId of(UUID value) { return new HorarioRecoleccionId(value); }
    public static HorarioRecoleccionId of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("HorarioRecoleccionId no puede ser nulo o vacío");
        try { return new HorarioRecoleccionId(UUID.fromString(value)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("HorarioRecoleccionId inválido: " + value); }
    }
    @Override public String toString() { return value.toString(); }
}
