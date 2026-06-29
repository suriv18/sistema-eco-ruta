package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.BaseJpaEntity;

import java.time.Instant;

@Entity
@Table(name = "permiso", schema = "auth")
public class PermisoJpaEntity extends BaseJpaEntity {

    @Column(name = "codigo", nullable = false, unique = true, length = 100)
    private String codigo;

    @Column(name = "modulo", nullable = false, length = 80)
    private String modulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "creado_en", updatable = false, nullable = false)
    private Instant creadoEn;

    @PrePersist
    protected void onPermisoCreate() {
        if (creadoEn == null) creadoEn = Instant.now();
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Instant getCreadoEn() { return creadoEn; }
}
