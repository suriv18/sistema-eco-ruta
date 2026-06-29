package pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record TenantId(UUID value) implements ValueObject {

    public TenantId {
        if (value == null) {
            throw new IllegalArgumentException("TenantId no puede ser nulo");
        }
    }

    public static TenantId of(UUID value) {
        return new TenantId(value);
    }

    public static TenantId of(String value) {
        try {
            return new TenantId(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("TenantId inválido: " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
