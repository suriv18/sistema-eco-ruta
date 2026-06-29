package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.util.UUID;

@Entity
@Table(name = "zona", schema = "operacion")
public class ZonaJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "distrito_id", nullable = false)
    private UUID distritoId;

    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "tipo_zona", nullable = false, length = 30)
    private String tipoZona;

    @Column(name = "prioridad_base", nullable = false)
    private int prioridadBase;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    public UUID getDistritoId() { return distritoId; }
    public void setDistritoId(UUID distritoId) { this.distritoId = distritoId; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoZona() { return tipoZona; }
    public void setTipoZona(String tipoZona) { this.tipoZona = tipoZona; }
    public int getPrioridadBase() { return prioridadBase; }
    public void setPrioridadBase(int prioridadBase) { this.prioridadBase = prioridadBase; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
