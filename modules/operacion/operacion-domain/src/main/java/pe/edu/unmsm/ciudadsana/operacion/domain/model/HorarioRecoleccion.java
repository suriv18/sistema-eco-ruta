package pe.edu.unmsm.ciudadsana.operacion.domain.model;

import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoHorario;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.HorarioRecoleccionRegistradoEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.time.LocalTime;

public class HorarioRecoleccion extends AggregateRoot<HorarioRecoleccionId> {

    private final HorarioRecoleccionId id;
    private final TenantId tenantId;
    private final ZonaId zonaId;
    private final int diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String observacion;
    private EstadoHorario estado;
    private final Instant creadoEn;
    private Instant actualizadoEn;

    private HorarioRecoleccion(HorarioRecoleccionId id, TenantId tenantId, ZonaId zonaId, int diaSemana,
                                LocalTime horaInicio, LocalTime horaFin, String observacion,
                                EstadoHorario estado, Instant creadoEn, Instant actualizadoEn) {
        this.id = id;
        this.tenantId = tenantId;
        this.zonaId = zonaId;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.observacion = observacion;
        this.estado = estado;
        this.creadoEn = creadoEn;
        this.actualizadoEn = actualizadoEn;
    }

    public static HorarioRecoleccion create(HorarioRecoleccionId id, TenantId tenantId, ZonaId zonaId,
                                             int diaSemana, LocalTime horaInicio, LocalTime horaFin,
                                             String observacion, Instant creadoEn) {
        if (id == null) throw new IllegalArgumentException("HorarioRecoleccionId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (zonaId == null) throw new IllegalArgumentException("ZonaId no puede ser nulo");
        if (diaSemana < 1 || diaSemana > 7) throw new IllegalArgumentException("diaSemana debe estar entre 1 y 7");
        if (horaInicio == null) throw new IllegalArgumentException("horaInicio no puede ser nulo");
        if (horaFin == null) throw new IllegalArgumentException("horaFin no puede ser nulo");
        if (!horaFin.isAfter(horaInicio)) throw new IllegalArgumentException("horaFin debe ser posterior a horaInicio");
        if (creadoEn == null) throw new IllegalArgumentException("creadoEn no puede ser nulo");
        HorarioRecoleccion h = new HorarioRecoleccion(id, tenantId, zonaId, diaSemana, horaInicio, horaFin,
                observacion, EstadoHorario.ACTIVO, creadoEn, null);
        h.recordDomainEvent(new HorarioRecoleccionRegistradoEvent(id.value(), creadoEn, tenantId.value(), zonaId.value(), diaSemana));
        return h;
    }

    public static HorarioRecoleccion reconstitute(HorarioRecoleccionId id, TenantId tenantId, ZonaId zonaId,
                                                   int diaSemana, LocalTime horaInicio, LocalTime horaFin,
                                                   String observacion, EstadoHorario estado, Instant creadoEn,
                                                   Instant actualizadoEn) {
        return new HorarioRecoleccion(id, tenantId, zonaId, diaSemana, horaInicio, horaFin,
                observacion, estado, creadoEn, actualizadoEn);
    }

    public void actualizar(LocalTime horaInicio, LocalTime horaFin, String observacion) {
        if (!horaFin.isAfter(horaInicio)) throw new IllegalArgumentException("horaFin debe ser posterior a horaInicio");
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.observacion = observacion;
        this.actualizadoEn = Instant.now();
    }

    public void desactivar() {
        if (estado == EstadoHorario.INACTIVO) throw new IllegalStateException("El horario ya está INACTIVO");
        estado = EstadoHorario.INACTIVO;
        this.actualizadoEn = Instant.now();
    }

    @Override
    public HorarioRecoleccionId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public ZonaId getZonaId() { return zonaId; }
    public int getDiaSemana() { return diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public String getObservacion() { return observacion; }
    public EstadoHorario getEstado() { return estado; }
    public Instant getCreadoEn() { return creadoEn; }
    public Instant getActualizadoEn() { return actualizadoEn; }
}
