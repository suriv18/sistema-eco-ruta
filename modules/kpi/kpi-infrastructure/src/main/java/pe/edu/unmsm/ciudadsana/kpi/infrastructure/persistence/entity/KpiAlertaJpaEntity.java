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
import java.util.UUID;

@Entity
@Table(name = "kpi_alerta", schema = "kpi")
@Getter
@Setter
@NoArgsConstructor
public class KpiAlertaJpaEntity {

    @Id
    @Column(name = "kpi_alerta_id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "alerta_id_externo", nullable = false)
    private UUID alertaIdExterno;

    @Column(name = "zona_id_externo")
    private UUID zonaIdExterno;

    @Column(name = "registrada_en", nullable = false)
    private Instant registradaEn;

    @Column(name = "atendida_en")
    private Instant atendidaEn;

    @Column(name = "tiempo_respuesta_min")
    private BigDecimal tiempoRespuestaMin;

    @Column(name = "fue_critica", nullable = false)
    private boolean fueCritica;

    @Column(name = "incluida_en_ruta", nullable = false)
    private boolean incluidaEnRuta;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;
}
