package pe.edu.unmsm.ciudadsana.operacion.domain.model;

import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.TurnoCreadoEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.TurnoFinalizadoEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.TurnoIniciadoEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public class Turno extends AggregateRoot<TurnoId> {

    private final TurnoId id;
    private final TenantId tenantId;
    private final UnidadId unidadId;
    private final ChoferId choferId;
    private final DistritoId distritoId;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private final TipoTurno tipo;
    private EstadoTurno estado;
    private final Instant creadoEn;

    private Turno(TurnoId id, TenantId tenantId, UnidadId unidadId, ChoferId choferId, DistritoId distritoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, TipoTurno tipo, EstadoTurno estado, Instant creadoEn) {
        this.id = id;
        this.tenantId = tenantId;
        this.unidadId = unidadId;
        this.choferId = choferId;
        this.distritoId = distritoId;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.tipo = tipo;
        this.estado = estado;
        this.creadoEn = creadoEn;
    }

    public static Turno create(TurnoId id, TenantId tenantId, UnidadId unidadId, ChoferId choferId, DistritoId distritoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, TipoTurno tipo, Instant creadoEn) {
        if (id == null) throw new IllegalArgumentException("TurnoId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (unidadId == null) throw new IllegalArgumentException("UnidadId no puede ser nulo");
        if (choferId == null) throw new IllegalArgumentException("ChoferId no puede ser nulo");
        if (distritoId == null) throw new IllegalArgumentException("DistritoId no puede ser nulo");
        if (fecha == null) throw new IllegalArgumentException("Fecha no puede ser nula");
        if (horaInicio == null) throw new IllegalArgumentException("HoraInicio no puede ser nula");
        if (horaFin == null) throw new IllegalArgumentException("HoraFin no puede ser nula");
        if (tipo == null) throw new IllegalArgumentException("TipoTurno no puede ser nulo");
        if (creadoEn == null) throw new IllegalArgumentException("creadoEn no puede ser nulo");
        if (!horaFin.isAfter(horaInicio)) throw new IllegalArgumentException("HoraFin debe ser posterior a HoraInicio");
        Turno t = new Turno(id, tenantId, unidadId, choferId, distritoId, fecha, horaInicio, horaFin, tipo, EstadoTurno.PROGRAMADO, creadoEn);
        t.recordDomainEvent(new TurnoCreadoEvent(id.value(), creadoEn, tenantId.value(), unidadId.value(), choferId.value()));
        return t;
    }

    public static Turno reconstitute(TurnoId id, TenantId tenantId, UnidadId unidadId, ChoferId choferId, DistritoId distritoId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, TipoTurno tipo, EstadoTurno estado, Instant creadoEn) {
        return new Turno(id, tenantId, unidadId, choferId, distritoId, fecha, horaInicio, horaFin, tipo, estado, creadoEn);
    }

    public void iniciar(Instant ahora) {
        if (estado != EstadoTurno.PROGRAMADO) throw new IllegalStateException("Solo se puede iniciar un turno PROGRAMADO. Estado actual: " + estado);
        estado = EstadoTurno.EN_CURSO;
        recordDomainEvent(new TurnoIniciadoEvent(id.value(), ahora, tenantId.value()));
    }

    public void finalizar(Instant ahora) {
        if (estado != EstadoTurno.EN_CURSO) throw new IllegalStateException("Solo se puede finalizar un turno EN_CURSO. Estado actual: " + estado);
        estado = EstadoTurno.FINALIZADO;
        recordDomainEvent(new TurnoFinalizadoEvent(id.value(), ahora, tenantId.value()));
    }

    public void cancelar() {
        if (estado != EstadoTurno.PROGRAMADO && estado != EstadoTurno.EN_CURSO) throw new IllegalStateException("No se puede cancelar un turno en estado: " + estado);
        estado = EstadoTurno.CANCELADO;
    }

    public boolean seSuperponeCon(LocalDate fecha, LocalTime inicio, LocalTime fin) {
        return this.fecha.equals(fecha) && this.horaInicio.isBefore(fin) && this.horaFin.isAfter(inicio);
    }

    @Override
    public TurnoId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public UnidadId getUnidadId() { return unidadId; }
    public ChoferId getChoferId() { return choferId; }
    public DistritoId getDistritoId() { return distritoId; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public TipoTurno getTipo() { return tipo; }
    public EstadoTurno getEstado() { return estado; }
    public Instant getCreadoEn() { return creadoEn; }
}
