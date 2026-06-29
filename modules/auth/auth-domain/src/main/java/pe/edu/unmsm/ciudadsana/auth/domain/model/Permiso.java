package pe.edu.unmsm.ciudadsana.auth.domain.model;

import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;

public class Permiso {

    private final PermisoId id;
    private final String codigo;
    private final String modulo;
    private final String descripcion;

    private Permiso(PermisoId id, String codigo, String modulo, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.modulo = modulo;
        this.descripcion = descripcion;
    }

    public static Permiso create(PermisoId id, String codigo, String modulo, String descripcion) {
        if (id == null) throw new IllegalArgumentException("PermisoId no puede ser nulo");
        if (codigo == null || codigo.isBlank()) throw new IllegalArgumentException("El código del permiso no puede ser nulo o vacío");
        if (modulo == null || modulo.isBlank()) throw new IllegalArgumentException("El módulo del permiso no puede ser nulo o vacío");
        return new Permiso(id, codigo, modulo, descripcion);
    }

    public PermisoId getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getModulo() {
        return modulo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
