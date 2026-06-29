package pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ruta", schema = "operacion")
@Getter
@Setter
@NoArgsConstructor
public class RutaJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "turno_id", nullable = false)
    private UUID turnoId;

    @Column(name = "distrito_id", nullable = false)
    private UUID distritoId;

    @Column(name = "deposito_origen_id", nullable = false)
    private UUID depositoOrigenId;

    @Column(name = "deposito_destino_id", nullable = false)
    private UUID depositoDestinoId;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "tipo_ruta", nullable = false)
    private String tipoRuta;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "distancia_total_m")
    private double distanciaTotalM;

    @Column(name = "duracion_total_s")
    private int duracionTotalS;

    @Column(name = "carga_total_kg")
    private double cargaTotalKg;

    // NOTE: geometria column exists in DB but is NOT mapped in JPA — skipped intentionally
}
