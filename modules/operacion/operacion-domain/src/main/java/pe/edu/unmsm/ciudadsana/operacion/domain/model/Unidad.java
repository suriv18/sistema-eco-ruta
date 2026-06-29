package pe.edu.unmsm.ciudadsana.operacion.domain.model;

import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoOperativoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.UnidadEstadoCambiadoEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.UnidadRegistradaEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadKg;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadM3;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.Placa;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;

public class Unidad extends AggregateRoot<UnidadId> {

    private final UnidadId id;
    private final TenantId tenantId;
    private final Placa placa;
    private final String codigoInterno;
    private final TipoUnidad tipo;
    private final CapacidadM3 capacidadM3;
    private final CapacidadKg capacidadKg;
    private EstadoOperativoUnidad estadoOperativo;
    private final Instant creadoEn;

    private Unidad(UnidadId id, TenantId tenantId, Placa placa, String codigoInterno, TipoUnidad tipo, CapacidadM3 capacidadM3, CapacidadKg capacidadKg, EstadoOperativoUnidad estadoOperativo, Instant creadoEn) {
        this.id = id;
        this.tenantId = tenantId;
        this.placa = placa;
        this.codigoInterno = codigoInterno;
        this.tipo = tipo;
        this.capacidadM3 = capacidadM3;
        this.capacidadKg = capacidadKg;
        this.estadoOperativo = estadoOperativo;
        this.creadoEn = creadoEn;
    }

    public static Unidad create(UnidadId id, TenantId tenantId, Placa placa, String codigoInterno, TipoUnidad tipo, CapacidadM3 capacidadM3, CapacidadKg capacidadKg, Instant creadoEn) {
        if (id == null) throw new IllegalArgumentException("UnidadId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (placa == null) throw new IllegalArgumentException("Placa no puede ser nula");
        if (codigoInterno == null || codigoInterno.isBlank()) throw new IllegalArgumentException("CodigoInterno no puede ser nulo o vacío");
        if (tipo == null) throw new IllegalArgumentException("TipoUnidad no puede ser nulo");
        if (capacidadM3 == null) throw new IllegalArgumentException("CapacidadM3 no puede ser nula");
        if (capacidadKg == null) throw new IllegalArgumentException("CapacidadKg no puede ser nula");
        if (creadoEn == null) throw new IllegalArgumentException("creadoEn no puede ser nulo");
        Unidad u = new Unidad(id, tenantId, placa, codigoInterno.trim(), tipo, capacidadM3, capacidadKg, EstadoOperativoUnidad.OPERATIVA, creadoEn);
        u.recordDomainEvent(new UnidadRegistradaEvent(id.value(), creadoEn, tenantId.value(), placa.value()));
        return u;
    }

    public static Unidad reconstitute(UnidadId id, TenantId tenantId, Placa placa, String codigoInterno, TipoUnidad tipo, CapacidadM3 capacidadM3, CapacidadKg capacidadKg, EstadoOperativoUnidad estadoOperativo, Instant creadoEn) {
        return new Unidad(id, tenantId, placa, codigoInterno, tipo, capacidadM3, capacidadKg, estadoOperativo, creadoEn);
    }

    public void cambiarEstadoOperativo(EstadoOperativoUnidad nuevoEstado) {
        if (nuevoEstado == null) throw new IllegalArgumentException("EstadoOperativoUnidad no puede ser nulo");
        if (estadoOperativo == nuevoEstado) throw new IllegalStateException("La unidad ya está en estado " + nuevoEstado);
        recordDomainEvent(new UnidadEstadoCambiadoEvent(id.value(), Instant.now(), tenantId.value(), estadoOperativo.name(), nuevoEstado.name()));
        this.estadoOperativo = nuevoEstado;
    }

    public boolean estaDisponible() {
        return estadoOperativo == EstadoOperativoUnidad.OPERATIVA;
    }

    @Override
    public UnidadId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public Placa getPlaca() { return placa; }
    public String getCodigoInterno() { return codigoInterno; }
    public TipoUnidad getTipo() { return tipo; }
    public CapacidadM3 getCapacidadM3() { return capacidadM3; }
    public CapacidadKg getCapacidadKg() { return capacidadKg; }
    public EstadoOperativoUnidad getEstadoOperativo() { return estadoOperativo; }
    public Instant getCreadoEn() { return creadoEn; }
}
