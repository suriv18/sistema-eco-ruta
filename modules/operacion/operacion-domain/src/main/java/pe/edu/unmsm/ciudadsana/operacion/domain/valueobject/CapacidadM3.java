package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.math.BigDecimal;

public record CapacidadM3(BigDecimal value) implements ValueObject {
    public CapacidadM3 {
        if (value == null) throw new IllegalArgumentException("CapacidadM3 no puede ser nula");
        if (value.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("CapacidadM3 debe ser mayor a 0");
    }
    public static CapacidadM3 of(BigDecimal value) { return new CapacidadM3(value); }
    @Override public String toString() { return value.toPlainString(); }
}
