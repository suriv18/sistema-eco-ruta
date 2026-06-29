package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record ContenedorExternoId(UUID value) implements ValueObject {
    public ContenedorExternoId {
        Objects.requireNonNull(value, "ContenedorExternoId value must not be null");
    }

    public static ContenedorExternoId of(UUID value) {
        return new ContenedorExternoId(value);
    }

    public static ContenedorExternoId of(String value) {
        return new ContenedorExternoId(UUID.fromString(value));
    }
}
