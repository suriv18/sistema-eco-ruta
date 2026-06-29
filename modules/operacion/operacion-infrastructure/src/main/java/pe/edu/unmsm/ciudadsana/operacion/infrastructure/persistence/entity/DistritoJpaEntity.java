package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

@Entity
@Table(name = "distrito", schema = "operacion")
public class DistritoJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "ubigeo", length = 6)
    private String ubigeo;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUbigeo() { return ubigeo; }
    public void setUbigeo(String ubigeo) { this.ubigeo = ubigeo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
