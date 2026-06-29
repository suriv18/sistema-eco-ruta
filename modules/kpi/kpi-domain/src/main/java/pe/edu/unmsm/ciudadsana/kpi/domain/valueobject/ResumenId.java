package pe.edu.unmsm.ciudadsana.kpi.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record ResumenId(UUID value) implements ValueObject {

    public ResumenId {
        if (value == null) throw new IllegalArgumentException("ResumenId no puede ser nulo");
    }

    public static ResumenId of(UUID value) { return new ResumenId(value); }
    public static ResumenId generate() { return new ResumenId(UUID.randomUUID()); }
}
