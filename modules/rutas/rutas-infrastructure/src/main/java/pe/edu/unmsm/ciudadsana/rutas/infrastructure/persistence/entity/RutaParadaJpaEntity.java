package pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.BaseJpaEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ruta_parada", schema = "operacion")
@Getter
@Setter
@NoArgsConstructor
public class RutaParadaJpaEntity extends BaseJpaEntity {

    @Column(name = "ruta_version_id", nullable = false)
    private UUID rutaVersionId;

    @Column(name = "zona_id", nullable = false)
    private UUID zonaId;

    @Column(name = "contenedor_id")
    private UUID contenedorId;

    @Column(name = "orden", nullable = false)
    private int orden;

    @Column(name = "eta")
    private Instant eta;

    @Column(name = "hora_llegada_real")
    private Instant horaLlegadaReal;

    @Column(name = "hora_salida_real")
    private Instant horaSalidaReal;

    @Column(name = "demanda_estimada_kg")
    private double demandaEstimadaKg;

    @Column(name = "carga_acumulada_kg")
    private double cargaAcumuladaKg;

    @Column(name = "estado", nullable = false)
    private String estado;
}
