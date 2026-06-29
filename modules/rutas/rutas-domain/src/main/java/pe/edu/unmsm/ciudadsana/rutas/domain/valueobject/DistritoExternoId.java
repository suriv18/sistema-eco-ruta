package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record DistritoExternoId(UUID value) implements ValueObject {
    public DistritoExternoId {
        Objects.requireNonNull(value, "DistritoExternoId value must not be null");
    }

    public static DistritoExternoId of(UUID value) {
        return new DistritoExternoId(value);
    }

    public static DistritoExternoId of(String value) {
        return new DistritoExternoId(UUID.fromString(value));
    }
}
