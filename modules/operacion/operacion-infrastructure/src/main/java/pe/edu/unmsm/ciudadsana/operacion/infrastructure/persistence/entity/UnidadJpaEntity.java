package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "unidad", schema = "operacion")
public class UnidadJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "placa", nullable = false, length = 15)
    private String placa;

    @Column(name = "codigo_interno", length = 50)
    private String codigoInterno;

    @Column(name = "tipo_unidad", nullable = false, length = 50)
    private String tipoUnidad;

    @Column(name = "capacidad_m3", nullable = false, precision = 8, scale = 2)
    private BigDecimal capacidadM3;

    @Column(name = "capacidad_kg", nullable = false, precision = 10, scale = 2)
    private BigDecimal capacidadKg;

    @Column(name = "estado_operativo", nullable = false, length = 30)
    private String estadoOperativo;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }
    public String getTipoUnidad() { return tipoUnidad; }
    public void setTipoUnidad(String tipoUnidad) { this.tipoUnidad = tipoUnidad; }
    public BigDecimal getCapacidadM3() { return capacidadM3; }
    public void setCapacidadM3(BigDecimal capacidadM3) { this.capacidadM3 = capacidadM3; }
    public BigDecimal getCapacidadKg() { return capacidadKg; }
    public void setCapacidadKg(BigDecimal capacidadKg) { this.capacidadKg = capacidadKg; }
    public String getEstadoOperativo() { return estadoOperativo; }
    public void setEstadoOperativo(String estadoOperativo) { this.estadoOperativo = estadoOperativo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
