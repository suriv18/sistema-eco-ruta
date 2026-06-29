package pe.edu.unmsm.ciudadsana.telemetria.domain.model;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.enums.TipoEventoConectividad;
import pe.edu.unmsm.ciudadsana.telemetria.domain.event.EventoConectividadRegistradoEvent;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.ConectividadId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.DispositivoId;
import pe.edu.unmsm.ciudadsana.telemetria.domain.valueobject.UnidadExternoId;

import java.time.Instant;
import java.util.Optional;

public class EventoConectividad extends AggregateRoot<ConectividadId> {

    private final ConectividadId id;
    private final TenantId tenantId;
    private final UnidadExternoId unidadExternoId;
    private final Optional<DispositivoId> dispositivoId;
    private final TipoEventoConectividad tipoEvento;
    private final Optional<String> detalle;
    private final Instant detectadoEn;

    private EventoConectividad(
            ConectividadId id,
            TenantId tenantId,
            UnidadExternoId unidadExternoId,
            Optional<DispositivoId> dispositivoId,
            TipoEventoConectividad tipoEvento,
            Optional<String> detalle,
            Instant detectadoEn
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.unidadExternoId = unidadExternoId;
        this.dispositivoId = dispositivoId;
        this.tipoEvento = tipoEvento;
        this.detalle = detalle;
        this.detectadoEn = detectadoEn;
    }

    public static EventoConectividad create(
            ConectividadId id,
            TenantId tenantId,
            UnidadExternoId unidadExternoId,
            Optional<DispositivoId> dispositivoId,
            TipoEventoConectividad tipoEvento,
            Optional<String> detalle,
            Instant detectadoEn
    ) {
        if (id == null) throw new IllegalArgumentException("ConectividadId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (unidadExternoId == null) throw new IllegalArgumentException("UnidadExternoId no puede ser nulo");
        if (dispositivoId == null) throw new IllegalArgumentException("dispositivoId no puede ser nulo (use Optional.empty())");
        if (tipoEvento == null) throw new IllegalArgumentException("El tipo de evento no puede ser nulo");
        if (detalle == null) throw new IllegalArgumentException("detalle no puede ser nulo (use Optional.empty())");
        if (detectadoEn == null) throw new IllegalArgumentException("La fecha de detección no puede ser nula");

        EventoConectividad evento = new EventoConectividad(
                id, tenantId, unidadExternoId, dispositivoId,
                tipoEvento, detalle, detectadoEn
        );

        evento.recordDomainEvent(new EventoConectividadRegistradoEvent(
                id.value(),
                detectadoEn,
                tenantId.value(),
                tipoEvento.name()
        ));

        return evento;
    }

    public static EventoConectividad reconstitute(
            ConectividadId id,
            TenantId tenantId,
            UnidadExternoId unidadExternoId,
            Optional<DispositivoId> dispositivoId,
            TipoEventoConectividad tipoEvento,
            Optional<String> detalle,
            Instant detectadoEn
    ) {
        return new EventoConectividad(
                id, tenantId, unidadExternoId, dispositivoId,
                tipoEvento, detalle, detectadoEn
        );
    }

    @Override
    public ConectividadId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public UnidadExternoId getUnidadExternoId() {
        return unidadExternoId;
    }

    public Optional<DispositivoId> getDispositivoId() {
        return dispositivoId;
    }

    public TipoEventoConectividad getTipoEvento() {
        return tipoEvento;
    }

    public Optional<String> getDetalle() {
        return detalle;
    }

    public Instant getDetectadoEn() {
        return detectadoEn;
    }
}
