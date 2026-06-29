package pe.edu.unmsm.ciudadsana.operacion.domain.model;

import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.ZonaDesactivadaEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.ZonaRegistradaEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CodigoZona;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.PrioridadBase;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;

public class Zona extends AggregateRoot<ZonaId> {

    private final ZonaId id;
    private final TenantId tenantId;
    private final DistritoId distritoId;
    private final CodigoZona codigo;
    private final String nombre;
    private final TipoZona tipo;
    private PrioridadBase prioridad;
    private EstadoZona estado;
    private final Instant creadoEn;

    private Zona(ZonaId id, TenantId tenantId, DistritoId distritoId, CodigoZona codigo, String nombre, TipoZona tipo, PrioridadBase prioridad, EstadoZona estado, Instant creadoEn) {
        this.id = id;
        this.tenantId = tenantId;
        this.distritoId = distritoId;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.prioridad = prioridad;
        this.estado = estado;
        this.creadoEn = creadoEn;
    }

    public static Zona create(ZonaId id, TenantId tenantId, DistritoId distritoId, CodigoZona codigo, String nombre, TipoZona tipo, PrioridadBase prioridad, Instant creadoEn) {
        if (id == null) throw new IllegalArgumentException("ZonaId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (distritoId == null) throw new IllegalArgumentException("DistritoId no puede ser nulo");
        if (codigo == null) throw new IllegalArgumentException("CodigoZona no puede ser nulo");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre no puede ser nulo o vacío");
        if (tipo == null) throw new IllegalArgumentException("TipoZona no puede ser nulo");
        if (prioridad == null) throw new IllegalArgumentException("PrioridadBase no puede ser nulo");
        if (creadoEn == null) throw new IllegalArgumentException("creadoEn no puede ser nulo");
        Zona z = new Zona(id, tenantId, distritoId, codigo, nombre.trim(), tipo, prioridad, EstadoZona.ACTIVA, creadoEn);
        z.recordDomainEvent(new ZonaRegistradaEvent(id.value(), creadoEn, tenantId.value(), codigo.value()));
        return z;
    }

    public static Zona reconstitute(ZonaId id, TenantId tenantId, DistritoId distritoId, CodigoZona codigo, String nombre, TipoZona tipo, PrioridadBase prioridad, EstadoZona estado, Instant creadoEn) {
        return new Zona(id, tenantId, distritoId, codigo, nombre, tipo, prioridad, estado, creadoEn);
    }

    public void desactivar() {
        if (estado == EstadoZona.INACTIVA) throw new IllegalStateException("La zona ya está INACTIVA");
        estado = EstadoZona.INACTIVA;
        recordDomainEvent(new ZonaDesactivadaEvent(id.value(), Instant.now(), tenantId.value()));
    }

    public void actualizarPrioridad(PrioridadBase nuevaPrioridad) {
        if (nuevaPrioridad == null) throw new IllegalArgumentException("PrioridadBase no puede ser nulo");
        this.prioridad = nuevaPrioridad;
    }

    @Override
    public ZonaId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public DistritoId getDistritoId() { return distritoId; }
    public CodigoZona getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public TipoZona getTipo() { return tipo; }
    public PrioridadBase getPrioridad() { return prioridad; }
    public EstadoZona getEstado() { return estado; }
    public Instant getCreadoEn() { return creadoEn; }
}
