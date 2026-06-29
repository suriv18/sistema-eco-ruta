package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.BaseJpaEntity;

import java.util.UUID;

@Entity
@Table(name = "alerta_foto", schema = "ciudadano")
@Getter
@Setter
@NoArgsConstructor
public class AlertaFotoJpaEntity extends BaseJpaEntity {

    @Column(name = "alerta_id", nullable = false)
    private UUID alertaId;

    @Column(name = "url_archivo", nullable = false, columnDefinition = "TEXT")
    private String urlArchivo;

    @Column(name = "tipo_mime", nullable = false)
    private String tipoMime;

    @Column(name = "tamanio_bytes")
    private Long tamanioBytes;
}
