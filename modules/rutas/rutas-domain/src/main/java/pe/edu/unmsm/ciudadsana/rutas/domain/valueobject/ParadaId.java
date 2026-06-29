package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record ParadaId(UUID value) implements ValueObject {
    public ParadaId {
        Objects.requireNonNull(value, "ParadaId value must not be null");
    }

    public static ParadaId of(UUID value) {
        return new ParadaId(value);
    }

    public static ParadaId of(String value) {
        return new ParadaId(UUID.fromString(value));
    }
}
