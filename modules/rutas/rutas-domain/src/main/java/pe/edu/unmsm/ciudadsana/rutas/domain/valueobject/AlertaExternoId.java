package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record AlertaExternoId(UUID value) implements ValueObject {
    public AlertaExternoId {
        Objects.requireNonNull(value, "AlertaExternoId value must not be null");
    }

    public static AlertaExternoId of(UUID value) {
        return new AlertaExternoId(value);
    }

    public static AlertaExternoId of(String value) {
        return new AlertaExternoId(UUID.fromString(value));
    }
}
