package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.TenantAwareJpaEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alerta_ciudadana", schema = "ciudadano")
@Getter
@Setter
@NoArgsConstructor
public class AlertaCiudadanaJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "ciudadano_id")
    private UUID ciudadanoId;

    @Column(name = "distrito_id_externo", nullable = false)
    private UUID distritoIdExterno;

    @Column(name = "zona_id_externo")
    private UUID zonaIdExterno;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "volumen_estimado", nullable = false)
    private String volumenEstimado;

    @Column(name = "nivel_criticidad", nullable = false)
    private String nivelCriticidad;

    @Column(name = "fuente", nullable = false)
    private String fuente;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "registrada_en", nullable = false)
    private Instant registradaEn;

    @Column(name = "actualizada_en")
    private Instant actualizadaEn;
}
