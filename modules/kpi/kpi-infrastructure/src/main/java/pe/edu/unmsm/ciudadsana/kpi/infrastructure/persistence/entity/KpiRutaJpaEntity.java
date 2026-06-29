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
@Table(name = "kpi_ruta", schema = "kpi")
@Getter
@Setter
@NoArgsConstructor
public class KpiRutaJpaEntity {

    @Id
    @Column(name = "kpi_ruta_id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "ruta_id_externo", nullable = false)
    private UUID rutaIdExterno;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "distancia_planificada_m")
    private BigDecimal distanciaPlanificadaM;

    @Column(name = "distancia_real_m")
    private BigDecimal distanciaRealM;

    @Column(name = "duracion_planificada_s")
    private int duracionPlanificadaS;

    @Column(name = "duracion_real_s")
    private int duracionRealS;

    @Column(name = "zonas_programadas")
    private int zonasProgramadas;

    @Column(name = "zonas_atendidas")
    private int zonasAtendidas;

    @Column(name = "cumplimiento_porcentaje")
    private BigDecimal cumplimientoPorcentaje;

    @Column(name = "km_por_tonelada")
    private BigDecimal kmPorTonelada;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;
}
