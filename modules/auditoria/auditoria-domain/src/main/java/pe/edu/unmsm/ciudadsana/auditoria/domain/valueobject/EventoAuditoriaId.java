package pe.edu.unmsm.ciudadsana.auditoria.domain.valueobject;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.ValueObject;

import java.util.UUID;

public record EventoAuditoriaId(UUID value) implements ValueObject {

    public EventoAuditoriaId {
        if (value == null) throw new IllegalArgumentException("EventoAuditoriaId no puede ser nulo");
    }

    public static EventoAuditoriaId of(UUID value) { return new EventoAuditoriaId(value); }
    public static EventoAuditoriaId generate() { return new EventoAuditoriaId(UUID.randomUUID()); }
}
