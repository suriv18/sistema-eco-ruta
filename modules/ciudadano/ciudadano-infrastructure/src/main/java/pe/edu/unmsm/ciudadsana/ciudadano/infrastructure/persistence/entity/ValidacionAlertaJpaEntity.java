package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity;

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
@Table(name = "validacion_alerta", schema = "ciudadano")
@Getter
@Setter
@NoArgsConstructor
public class ValidacionAlertaJpaEntity {

    @Id
    @Column(name = "validacion_id")
    private UUID validacionId;

    @Column(name = "alerta_id", nullable = false)
    private UUID alertaId;

    @Column(name = "es_duplicada", nullable = false)
    private boolean esDuplicada;

    @Column(name = "alerta_original_id")
    private UUID alertaOriginalId;

    @Column(name = "dentro_geocerca", nullable = false)
    private boolean dentroGeocerca;

    @Column(name = "score_spam", nullable = false)
    private double scoreSpam;

    @Column(name = "resultado", nullable = false)
    private String resultado;

    @Column(name = "validada_en", nullable = false)
    private Instant validadaEn;
}
