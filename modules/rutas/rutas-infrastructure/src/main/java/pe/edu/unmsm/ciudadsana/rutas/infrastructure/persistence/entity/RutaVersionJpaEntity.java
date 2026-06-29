package pe.edu.unmsm.ciudadsana.rutas.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ruta_version", schema = "operacion")
@Getter
@Setter
@NoArgsConstructor
public class RutaVersionJpaEntity {

    @Id
    @Column(name = "ruta_version_id")
    private UUID id;

    @Column(name = "ruta_id", nullable = false)
    private UUID rutaId;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Column(name = "alerta_id_externo")
    private UUID alertaIdExterno;

    @Column(name = "generado_por", nullable = false)
    private String generadoPor;

    @Column(name = "distancia_total_m")
    private double distanciaTotalM;

    @Column(name = "duracion_total_s")
    private int duracionTotalS;

    @Column(name = "carga_total_kg")
    private double cargaTotalKg;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    // NOTE: geometria column exists in DB but is NOT mapped in JPA — skipped intentionally
}
