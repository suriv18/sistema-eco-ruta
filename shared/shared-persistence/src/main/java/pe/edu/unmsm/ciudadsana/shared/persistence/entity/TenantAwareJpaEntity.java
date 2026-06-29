package pe.edu.unmsm.ciudadsana.shared.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class TenantAwareJpaEntity extends BaseJpaEntity {

    @Column(name = "tenant_id", nullable = false, updatable = false)
    private UUID tenantId;

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }
}
