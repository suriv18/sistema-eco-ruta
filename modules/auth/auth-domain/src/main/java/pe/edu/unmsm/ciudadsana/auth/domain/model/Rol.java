package pe.edu.unmsm.ciudadsana.auth.domain.model;

import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;

public class Rol {

    private final RolId id;
    private final String codigo;
    private final String nombre;
    private final String descripcion;
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
        return new Rol(id, codigo, nombre, descripcion, true);
    }

    public static Rol reconstitute(RolId id, String codigo, String nombre, String descripcion, boolean activo) {
        return new Rol(id, codigo, nombre, descripcion, activo);
    }

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
