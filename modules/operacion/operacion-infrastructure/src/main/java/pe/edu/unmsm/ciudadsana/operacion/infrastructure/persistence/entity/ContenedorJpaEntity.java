package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "contenedor", schema = "operacion")
public class ContenedorJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "zona_id", nullable = false)
    private UUID zonaId;

    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "capacidad_m3", nullable = false, precision = 8, scale = 2)
    private BigDecimal capacidadM3;

    @Column(name = "estado_contenedor", nullable = false, length = 30)
    private String estadoContenedor;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    public UUID getZonaId() { return zonaId; }
    public void setZonaId(UUID zonaId) { this.zonaId = zonaId; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public BigDecimal getCapacidadM3() { return capacidadM3; }
    public void setCapacidadM3(BigDecimal capacidadM3) { this.capacidadM3 = capacidadM3; }
    public String getEstadoContenedor() { return estadoContenedor; }
    public void setEstadoContenedor(String estadoContenedor) { this.estadoContenedor = estadoContenedor; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
