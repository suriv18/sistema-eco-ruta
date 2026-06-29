package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record ZonaExternoId(UUID value) implements ValueObject {
    public ZonaExternoId {
        Objects.requireNonNull(value, "ZonaExternoId value must not be null");
    }

    public static ZonaExternoId of(UUID value) {
        return new ZonaExternoId(value);
    }

    public static ZonaExternoId of(String value) {
        return new ZonaExternoId(UUID.fromString(value));
    }
}
