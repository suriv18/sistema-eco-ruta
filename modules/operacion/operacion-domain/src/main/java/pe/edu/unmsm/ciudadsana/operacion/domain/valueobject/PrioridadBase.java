package pe.edu.unmsm.ciudadsana.operacion.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

public record PrioridadBase(int value) implements ValueObject {
    public PrioridadBase {
        if (value < 1) throw new IllegalArgumentException("PrioridadBase debe ser >= 1");
    }
    public static PrioridadBase of(int value) { return new PrioridadBase(value); }
    @Override public String toString() { return String.valueOf(value); }
}
