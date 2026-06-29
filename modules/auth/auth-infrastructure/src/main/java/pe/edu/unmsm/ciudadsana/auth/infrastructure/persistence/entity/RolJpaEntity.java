package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.BaseJpaEntity;

import java.time.Instant;

@Entity
@Table(name = "rol", schema = "auth")
public class RolJpaEntity extends BaseJpaEntity {

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @Column(name = "creado_en", updatable = false, nullable = false)
    private Instant creadoEn;

    @PrePersist
    protected void onRolCreate() {
        if (creadoEn == null) creadoEn = Instant.now();
        if (estado == null) estado = "ACTIVO";
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Instant getCreadoEn() { return creadoEn; }
}
