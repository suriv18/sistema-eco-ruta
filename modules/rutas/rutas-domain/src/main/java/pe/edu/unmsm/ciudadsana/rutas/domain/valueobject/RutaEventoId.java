package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record RutaEventoId(UUID value) implements ValueObject {
    public RutaEventoId {
        Objects.requireNonNull(value, "RutaEventoId value must not be null");
    }

    public static RutaEventoId of(UUID value) {
        return new RutaEventoId(value);
    }

    public static RutaEventoId of(String value) {
        return new RutaEventoId(UUID.fromString(value));
    }
}
