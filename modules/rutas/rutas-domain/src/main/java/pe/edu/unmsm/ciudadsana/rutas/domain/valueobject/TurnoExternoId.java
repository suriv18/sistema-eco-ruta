package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record TurnoExternoId(UUID value) implements ValueObject {
    public TurnoExternoId {
        Objects.requireNonNull(value, "TurnoExternoId value must not be null");
    }

    public static TurnoExternoId of(UUID value) {
        return new TurnoExternoId(value);
    }

    public static TurnoExternoId of(String value) {
        return new TurnoExternoId(UUID.fromString(value));
    }
}
