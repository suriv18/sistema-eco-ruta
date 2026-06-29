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
@Table(name = "kpi_zona", schema = "kpi")
@Getter
@Setter
@NoArgsConstructor
public class KpiZonaJpaEntity {

    @Id
    @Column(name = "kpi_zona_id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "zona_id_externo", nullable = false)
    private UUID zonaIdExterno;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "veces_programada")
    private int vecesProgramada;

    @Column(name = "veces_atendida")
    private int vecesAtendida;

    @Column(name = "kg_recolectados")
    private BigDecimal kgRecolectados;

    @Column(name = "cobertura_porcentaje")
    private BigDecimal coberturaPorcentaje;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;
}
