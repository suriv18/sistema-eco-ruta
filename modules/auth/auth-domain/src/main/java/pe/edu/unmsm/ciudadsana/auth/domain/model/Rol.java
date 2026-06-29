package pe.edu.unmsm.ciudadsana.auth.domain.model;

import pe.edu.unmsm.ciudadsana.auth.domain.event.RolCreadoEvent;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;

import java.time.Instant;

public class Rol extends AggregateRoot<RolId> {

    private final RolId id;
    private final String codigo;
    private String nombre;
    private String descripcion;
    private boolean activo;

    private Rol(RolId id, String codigo, String nombre, String descripcion, boolean activo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public static Rol create(RolId id, String codigo, String nombre, String descripcion) {
        if (id == null) throw new IllegalArgumentException("RolId no puede ser nulo");
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("El código del rol no puede ser nulo o vacío");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del rol no puede ser nulo o vacío");
        Rol rol = new Rol(id, codigo, nombre, descripcion, true);
        rol.recordDomainEvent(new RolCreadoEvent(id.value(), Instant.now(), codigo, nombre));
        return rol;
    }

    public static Rol reconstitute(RolId id, String codigo, String nombre, String descripcion, boolean activo) {
        return new Rol(id, codigo, nombre, descripcion, activo);
    }

    public void actualizar(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre del rol no puede ser nulo o vacío");
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public void desactivar() {
        this.activo = false;
    }

    @Override
    public RolId getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isActivo() {
        return activo;
    }
}
