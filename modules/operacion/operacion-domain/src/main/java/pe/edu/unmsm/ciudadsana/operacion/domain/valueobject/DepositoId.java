package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.util.UUID;

public record DepositoId(UUID value) implements ValueObject {
    public DepositoId {
        if (value == null) throw new IllegalArgumentException("DepositoId no puede ser nulo");
    }
    public static DepositoId of(UUID value) { return new DepositoId(value); }
    public static DepositoId of(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("DepositoId no puede ser nulo o vacío");
        try { return new DepositoId(UUID.fromString(value)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("DepositoId inválido: " + value); }
    }
    @Override public String toString() { return value.toString(); }
}
