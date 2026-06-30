package pe.edu.unmsm.ciudadsana.shared.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
public abstract class BaseJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "creado_en", updatable = false, nullable = false)
    private Instant creadoEn;

    @Column(name = "creado_por", updatable = false, nullable = false)
    private UUID creadoPor;

    @Column(name = "actualizado_en")
    private Instant actualizadoEn;

    @Column(name = "actualizado_por")
    private UUID actualizadoPor;

    private static final UUID SISTEMA_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @PrePersist
    protected void onCreate() {
        if (id == null) id = UUID.randomUUID();
        creadoEn = Instant.now();
        if (creadoPor == null) creadoPor = SISTEMA_ID;
        actualizadoEn = null;
    }

    @PreUpdate
    protected void onUpdate() {
        actualizadoEn = Instant.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Instant getCreadoEn() { return creadoEn; }
    public UUID getCreadoPor() { return creadoPor; }
    public void setCreadoPor(UUID creadoPor) { this.creadoPor = creadoPor; }
    public Instant getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(Instant actualizadoEn) { this.actualizadoEn = actualizadoEn; }
    public UUID getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(UUID actualizadoPor) { this.actualizadoPor = actualizadoPor; }
}
