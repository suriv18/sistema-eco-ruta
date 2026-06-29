package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.util.UUID;

@Entity
@Table(name = "deposito", schema = "operacion")
public class DepositoJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "distrito_id", nullable = false)
    private UUID distritoId;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "tipo", nullable = false, length = 40)
    private String tipo;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    public UUID getDistritoId() { return distritoId; }
    public void setDistritoId(UUID distritoId) { this.distritoId = distritoId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
