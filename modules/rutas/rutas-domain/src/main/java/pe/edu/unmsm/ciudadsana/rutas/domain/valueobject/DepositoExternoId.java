package pe.edu.unmsm.ciudadsana.rutas.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.Objects;
import java.util.UUID;

public record DepositoExternoId(UUID value) implements ValueObject {
    public DepositoExternoId {
        Objects.requireNonNull(value, "DepositoExternoId value must not be null");
    }

    public static DepositoExternoId of(UUID value) {
        return new DepositoExternoId(value);
    }

    public static DepositoExternoId of(String value) {
        return new DepositoExternoId(UUID.fromString(value));
    }
}
