package pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "resumen_operativo_diario", schema = "kpi")
@Getter
@Setter
@NoArgsConstructor
public class ResumenOperativoDiarioJpaEntity {

    @Id
    @Column(name = "resumen_id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "distrito_id_externo", nullable = false)
    private UUID distritoIdExterno;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "km_programados")
    private BigDecimal kmProgramados;

    @Column(name = "km_recorridos")
    private BigDecimal kmRecorridos;

    @Column(name = "toneladas_recolectadas")
    private BigDecimal toneladasRecolectadas;

    @Column(name = "cobertura_porcentaje")
    private BigDecimal coberturaPorcentaje;

    @Column(name = "alertas_registradas")
    private int alertasRegistradas;

    @Column(name = "alertas_atendidas")
    private int alertasAtendidas;

    @Column(name = "tiempo_respuesta_promedio_min")
    private BigDecimal tiempoRespuestaPromedioMin;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;
}
