package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;
import java.math.BigDecimal;

public record CapacidadKg(BigDecimal value) implements ValueObject {
    public CapacidadKg {
        if (value == null) throw new IllegalArgumentException("CapacidadKg no puede ser nula");
        if (value.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("CapacidadKg debe ser mayor a 0");
    }
    public static CapacidadKg of(BigDecimal value) { return new CapacidadKg(value); }
    @Override public String toString() { return value.toPlainString(); }
}
