package pe.edu.unmsm.ciudadsana.operacion.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "turno", schema = "operacion")
public class TurnoJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "unidad_id", nullable = false)
    private UUID unidadId;

    @Column(name = "chofer_id", nullable = false)
    private UUID choferId;

    @Column(name = "distrito_id", nullable = false)
    private UUID distritoId;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "tipo_turno", nullable = false, length = 30)
    private String tipoTurno;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado;

    public UUID getUnidadId() { return unidadId; }
    public void setUnidadId(UUID unidadId) { this.unidadId = unidadId; }
    public UUID getChoferId() { return choferId; }
    public void setChoferId(UUID choferId) { this.choferId = choferId; }
    public UUID getDistritoId() { return distritoId; }
    public void setDistritoId(UUID distritoId) { this.distritoId = distritoId; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public String getTipoTurno() { return tipoTurno; }
    public void setTipoTurno(String tipoTurno) { this.tipoTurno = tipoTurno; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
