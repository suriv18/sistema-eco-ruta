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
@Table(name = "alerta_estado_historial", schema = "ciudadano")
@Getter
@Setter
@NoArgsConstructor
public class AlertaHistorialJpaEntity {

    @Id
    @Column(name = "historial_id")
    private UUID historialId;

    @Column(name = "alerta_id", nullable = false)
    private UUID alertaId;

    @Column(name = "estado_anterior")
    private String estadoAnterior;

    @Column(name = "estado_nuevo", nullable = false)
    private String estadoNuevo;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "cambiado_por_usuario_id")
    private UUID cambiadoPorUsuarioId;

    @Column(name = "cambiado_en", nullable = false)
    private Instant cambiadoEn;
}
