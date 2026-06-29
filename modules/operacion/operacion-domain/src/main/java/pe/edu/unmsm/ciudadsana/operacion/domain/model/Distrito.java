package pe.edu.unmsm.ciudadsana.operacion.domain.model;

import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDistrito;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.DistritoRegistradoEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.Optional;

public class Distrito extends AggregateRoot<DistritoId> {

    private final DistritoId id;
    private final TenantId tenantId;
    private final String nombre;
    private final Optional<String> ubigeo;
    private EstadoDistrito estado;
    private final Instant creadoEn;

    private Distrito(DistritoId id, TenantId tenantId, String nombre, Optional<String> ubigeo, EstadoDistrito estado, Instant creadoEn) {
        this.id = id;
        this.tenantId = tenantId;
        this.nombre = nombre;
        this.ubigeo = ubigeo;
        this.estado = estado;
        this.creadoEn = creadoEn;
    }

    public static Distrito create(DistritoId id, TenantId tenantId, String nombre, String ubigeo, Instant creadoEn) {
        if (id == null) throw new IllegalArgumentException("DistritoId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre no puede ser nulo o vacío");
        if (creadoEn == null) throw new IllegalArgumentException("creadoEn no puede ser nulo");
        Distrito d = new Distrito(id, tenantId, nombre.trim(), Optional.ofNullable(ubigeo).filter(s -> !s.isBlank()), EstadoDistrito.ACTIVO, creadoEn);
        d.recordDomainEvent(new DistritoRegistradoEvent(id.value(), creadoEn, tenantId.value(), nombre.trim()));
        return d;
    }

    public static Distrito reconstitute(DistritoId id, TenantId tenantId, String nombre, String ubigeo, EstadoDistrito estado, Instant creadoEn) {
        return new Distrito(id, tenantId, nombre, Optional.ofNullable(ubigeo).filter(s -> !s.isBlank()), estado, creadoEn);
    }

    public void desactivar() {
        if (estado == EstadoDistrito.INACTIVO) throw new IllegalStateException("El distrito ya está INACTIVO");
        estado = EstadoDistrito.INACTIVO;
    }

    public void activar() {
        if (estado == EstadoDistrito.ACTIVO) throw new IllegalStateException("El distrito ya está ACTIVO");
        estado = EstadoDistrito.ACTIVO;
    }

    @Override
    public DistritoId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getNombre() { return nombre; }
    public Optional<String> getUbigeo() { return ubigeo; }
    public EstadoDistrito getEstado() { return estado; }
    public Instant getCreadoEn() { return creadoEn; }
}
