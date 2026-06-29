package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.BaseJpaEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token", schema = "auth")
public class RefreshTokenJpaEntity extends BaseJpaEntity {

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expira_en", nullable = false)
    private Instant expiraEn;

    @Column(name = "revocado", nullable = false)
    private boolean revocado;

    @Column(name = "creado_en", updatable = false, nullable = false)
    private Instant creadoEn;

    @Column(name = "revocado_en")
    private Instant revocadoEn;

    @PrePersist
    protected void onRefreshCreate() {
        if (creadoEn == null) creadoEn = Instant.now();
    }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(String tokenHash) { this.tokenHash = tokenHash; }
    public Instant getExpiraEn() { return expiraEn; }
    public void setExpiraEn(Instant expiraEn) { this.expiraEn = expiraEn; }
    public boolean isRevocado() { return revocado; }
    public void setRevocado(boolean revocado) { this.revocado = revocado; }
    public Instant getCreadoEn() { return creadoEn; }
    public Instant getRevocadoEn() { return revocadoEn; }
    public void setRevocadoEn(Instant revocadoEn) { this.revocadoEn = revocadoEn; }
}
