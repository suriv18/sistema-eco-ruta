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
@Table(name = "ruta_evento", schema = "operacion")
@Getter
@Setter
@NoArgsConstructor
public class RutaEventoJpaEntity {

    @Id
    @Column(name = "ruta_evento_id")
    private UUID id;

    @Column(name = "ruta_id", nullable = false)
    private UUID rutaId;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "datos", columnDefinition = "jsonb")
    private String datosJson;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    // NOTE: posicion column (geometry) exists in DB but is NOT mapped in JPA — skipped intentionally
}
