package pe.edu.unmsm.ciudadsana.kpi.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "kpi_unidad", schema = "kpi")
@Getter
@Setter
@NoArgsConstructor
public class KpiUnidadJpaEntity extends TenantAwareJpaEntity {

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
}
