package pe.edu.unmsm.ciudadsana.operacion.domain.model;

import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoContenedor;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.ContenedorEstadoCambiadoEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.ContenedorRegistradoEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadM3;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ContenedorId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;

public class Contenedor extends AggregateRoot<ContenedorId> {

    private final ContenedorId id;
    private final TenantId tenantId;
    private final ZonaId zonaId;
    private final String codigo;
    private final CapacidadM3 capacidad;
    private EstadoContenedor estadoContenedor;
    private final Instant creadoEn;

    private Contenedor(ContenedorId id, TenantId tenantId, ZonaId zonaId, String codigo, CapacidadM3 capacidad, EstadoContenedor estadoContenedor, Instant creadoEn) {
        this.id = id;
        this.tenantId = tenantId;
        this.zonaId = zonaId;
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.estadoContenedor = estadoContenedor;
        this.creadoEn = creadoEn;
    }

    public static Contenedor create(ContenedorId id, TenantId tenantId, ZonaId zonaId, String codigo, CapacidadM3 capacidad, Instant creadoEn) {
        if (id == null) throw new IllegalArgumentException("ContenedorId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (zonaId == null) throw new IllegalArgumentException("ZonaId no puede ser nulo");
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("Codigo no puede ser nulo o vacío");
        if (capacidad == null) throw new IllegalArgumentException("CapacidadM3 no puede ser nulo");
        if (creadoEn == null) throw new IllegalArgumentException("creadoEn no puede ser nulo");
        Contenedor c = new Contenedor(id, tenantId, zonaId, codigo.trim(), capacidad, EstadoContenedor.VACIO, creadoEn);
        c.recordDomainEvent(new ContenedorRegistradoEvent(id.value(), creadoEn, tenantId.value(), zonaId.value()));
        return c;
    }

    public static Contenedor reconstitute(ContenedorId id, TenantId tenantId, ZonaId zonaId, String codigo, CapacidadM3 capacidad, EstadoContenedor estadoContenedor, Instant creadoEn) {
        return new Contenedor(id, tenantId, zonaId, codigo, capacidad, estadoContenedor, creadoEn);
    }

    public void cambiarEstado(EstadoContenedor nuevoEstado) {
        if (nuevoEstado == null) throw new IllegalArgumentException("EstadoContenedor no puede ser nulo");
        this.estadoContenedor = nuevoEstado;
        recordDomainEvent(new ContenedorEstadoCambiadoEvent(id.value(), Instant.now(), tenantId.value(), nuevoEstado.name()));
    }

    @Override
    public ContenedorId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public ZonaId getZonaId() { return zonaId; }
    public String getCodigo() { return codigo; }
    public CapacidadM3 getCapacidad() { return capacidad; }
    public EstadoContenedor getEstadoContenedor() { return estadoContenedor; }
    public Instant getCreadoEn() { return creadoEn; }
}
