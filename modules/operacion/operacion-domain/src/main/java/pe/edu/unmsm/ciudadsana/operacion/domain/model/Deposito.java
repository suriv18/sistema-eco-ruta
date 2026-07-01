package pe.edu.unmsm.ciudadsana.operacion.domain.model;

import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoDeposito;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DepositoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;

public class Deposito {

    private final DepositoId id;
    private final TenantId tenantId;
    private final DistritoId distritoId;
    private final String nombre;
    private final TipoDeposito tipo;
    private EstadoDeposito estado;
    private final Instant creadoEn;

    private Deposito(DepositoId id, TenantId tenantId, DistritoId distritoId, String nombre, TipoDeposito tipo, EstadoDeposito estado, Instant creadoEn) {
        this.id = id;
        this.tenantId = tenantId;
        this.distritoId = distritoId;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.creadoEn = creadoEn;
    }

    public static Deposito create(DepositoId id, TenantId tenantId, DistritoId distritoId, String nombre, TipoDeposito tipo, Instant creadoEn) {
        if (id == null) throw new IllegalArgumentException("DepositoId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (distritoId == null) throw new IllegalArgumentException("DistritoId no puede ser nulo");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("Nombre no puede ser nulo o vacío");
        if (tipo == null) throw new IllegalArgumentException("TipoDeposito no puede ser nulo");
        if (creadoEn == null) throw new IllegalArgumentException("creadoEn no puede ser nulo");
        return new Deposito(id, tenantId, distritoId, nombre.trim(), tipo, EstadoDeposito.ACTIVO, creadoEn);
    }

    public static Deposito reconstitute(DepositoId id, TenantId tenantId, DistritoId distritoId, String nombre, TipoDeposito tipo, EstadoDeposito estado, Instant creadoEn) {
        return new Deposito(id, tenantId, distritoId, nombre, tipo, estado, creadoEn);
    }

    public void desactivar() {
        if (estado == EstadoDeposito.INACTIVO) throw new IllegalStateException("El depósito ya está INACTIVO");
        this.estado = EstadoDeposito.INACTIVO;
    }

    public DepositoId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public DistritoId getDistritoId() { return distritoId; }
    public String getNombre() { return nombre; }
    public TipoDeposito getTipo() { return tipo; }
    public EstadoDeposito getEstado() { return estado; }
    public Instant getCreadoEn() { return creadoEn; }
}
