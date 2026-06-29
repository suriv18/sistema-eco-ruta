package pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.entity;

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
@Table(name = "evento_auditoria", schema = "auditoria")
@Getter
@Setter
@NoArgsConstructor
public class EventoAuditoriaJpaEntity {

    @Id
    @Column(name = "evento_id")
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Column(name = "modulo", nullable = false, length = 80)
    private String modulo;

    @Column(name = "accion", nullable = false, length = 100)
    private String accion;

    @Column(name = "entidad", nullable = false, length = 100)
    private String entidad;

    @Column(name = "entidad_id")
    private UUID entidadId;

    @Column(name = "datos_antes", columnDefinition = "jsonb")
    private String datosAntes;

    @Column(name = "datos_despues", columnDefinition = "jsonb")
    private String datosDespues;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;
}
