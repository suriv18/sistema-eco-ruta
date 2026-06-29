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
@Table(name = "kpi_unidad", schema = "kpi")
@Getter
@Setter
@NoArgsConstructor
public class KpiUnidadJpaEntity {

    @Id
    @Column(name = "kpi_unidad_id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "unidad_id_externo", nullable = false)
    private UUID unidadIdExterno;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "km_recorridos")
    private BigDecimal kmRecorridos;

    @Column(name = "horas_operacion")
    private BigDecimal horasOperacion;

    @Column(name = "toneladas_recolectadas")
    private BigDecimal toneladasRecolectadas;

    @Column(name = "consumo_estimado_litros")
    private BigDecimal consumoEstimadoLitros;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;
}
