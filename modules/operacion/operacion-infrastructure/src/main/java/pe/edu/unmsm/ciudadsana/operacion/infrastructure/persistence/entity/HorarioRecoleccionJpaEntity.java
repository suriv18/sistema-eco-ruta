package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "horario_recoleccion", schema = "operacion")
public class HorarioRecoleccionJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "zona_id", nullable = false)
    private UUID zonaId;

    @Column(name = "dia_semana", nullable = false)
    private int diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    @PrePersist
    protected void onPrePersist() {
        if (estado == null) estado = "ACTIVO";
    }

    public UUID getZonaId() { return zonaId; }
    public void setZonaId(UUID zonaId) { this.zonaId = zonaId; }
    public int getDiaSemana() { return diaSemana; }
    public void setDiaSemana(int diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
