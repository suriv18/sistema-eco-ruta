package pe.edu.unmsm.ciudadsana.shared.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class AuditableJpaEntity extends TenantAwareJpaEntity {

    @Column(name = "creado_por")
    private UUID creadoPor;

    @Column(name = "actualizado_por")
    private UUID actualizadoPor;

    public UUID getCreadoPor() { return creadoPor; }
    public void setCreadoPor(UUID creadoPor) { this.creadoPor = creadoPor; }
    public UUID getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(UUID actualizadoPor) { this.actualizadoPor = actualizadoPor; }
}
