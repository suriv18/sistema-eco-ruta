package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record RutaId(UUID value) implements ValueObject {
    public RutaId {
        Objects.requireNonNull(value, "RutaId value must not be null");
    }

    public static RutaId of(UUID value) {
        return new RutaId(value);
    }

    public static RutaId of(String value) {
        return new RutaId(UUID.fromString(value));
    }
}
