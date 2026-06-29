package pe.edu.unmsm.ciudadsana.operacion.domain.model;

import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoChofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.event.ChoferRegistradoEvent;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.util.Optional;

public class Chofer extends AggregateRoot<ChoferId> {

    private final ChoferId id;
    private final TenantId tenantId;
    private final String nombres;
    private final String apellidos;
    private final Optional<String> dni;
    private final Optional<String> licencia;
    private final Optional<String> telefono;
    private EstadoChofer estado;
    private final Instant creadoEn;

    private Chofer(ChoferId id, TenantId tenantId, String nombres, String apellidos, Optional<String> dni, Optional<String> licencia, Optional<String> telefono, EstadoChofer estado, Instant creadoEn) {
        this.id = id;
        this.tenantId = tenantId;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.licencia = licencia;
        this.telefono = telefono;
        this.estado = estado;
        this.creadoEn = creadoEn;
    }

    public static Chofer create(ChoferId id, TenantId tenantId, String nombres, String apellidos, String dni, String licencia, String telefono, Instant creadoEn) {
        if (id == null) throw new IllegalArgumentException("ChoferId no puede ser nulo");
        if (tenantId == null) throw new IllegalArgumentException("TenantId no puede ser nulo");
        if (nombres == null || nombres.isBlank()) throw new IllegalArgumentException("Nombres no puede ser nulo o vacío");
        if (apellidos == null || apellidos.isBlank()) throw new IllegalArgumentException("Apellidos no puede ser nulo o vacío");
        if (creadoEn == null) throw new IllegalArgumentException("creadoEn no puede ser nulo");
        Chofer c = new Chofer(
                id,
                tenantId,
                nombres.trim(),
                apellidos.trim(),
                Optional.ofNullable(dni).filter(s -> !s.isBlank()),
                Optional.ofNullable(licencia).filter(s -> !s.isBlank()),
                Optional.ofNullable(telefono).filter(s -> !s.isBlank()),
                EstadoChofer.ACTIVO,
                creadoEn
        );
        c.recordDomainEvent(new ChoferRegistradoEvent(id.value(), creadoEn, tenantId.value(), nombres.trim(), apellidos.trim()));
        return c;
    }

    public static Chofer reconstitute(ChoferId id, TenantId tenantId, String nombres, String apellidos, String dni, String licencia, String telefono, EstadoChofer estado, Instant creadoEn) {
        return new Chofer(
                id,
                tenantId,
                nombres,
                apellidos,
                Optional.ofNullable(dni).filter(s -> !s.isBlank()),
                Optional.ofNullable(licencia).filter(s -> !s.isBlank()),
                Optional.ofNullable(telefono).filter(s -> !s.isBlank()),
                estado,
                creadoEn
        );
    }

    public void suspender() {
        if (estado == EstadoChofer.SUSPENDIDO) throw new IllegalStateException("El chofer ya está SUSPENDIDO");
        estado = EstadoChofer.SUSPENDIDO;
    }

    public void activar() {
        if (estado == EstadoChofer.ACTIVO) throw new IllegalStateException("El chofer ya está ACTIVO");
        estado = EstadoChofer.ACTIVO;
    }

    public void desactivar() {
        if (estado == EstadoChofer.INACTIVO) throw new IllegalStateException("El chofer ya está INACTIVO");
        estado = EstadoChofer.INACTIVO;
    }

    public boolean estaDisponible() {
        return estado == EstadoChofer.ACTIVO;
    }

    @Override
    public ChoferId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public Optional<String> getDni() { return dni; }
    public Optional<String> getLicencia() { return licencia; }
    public Optional<String> getTelefono() { return telefono; }
    public EstadoChofer getEstado() { return estado; }
    public Instant getCreadoEn() { return creadoEn; }
}
