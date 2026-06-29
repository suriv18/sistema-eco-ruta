package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record RutaVersionId(UUID value) implements ValueObject {
    public RutaVersionId {
        Objects.requireNonNull(value, "RutaVersionId value must not be null");
    }

    public static RutaVersionId of(UUID value) {
        return new RutaVersionId(value);
    }

    public static RutaVersionId of(String value) {
        return new RutaVersionId(UUID.fromString(value));
    }
}
