package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.unmsm.ciudadsana.shared.persistence.entity.BaseJpaEntity;

import java.util.UUID;

@Entity
@Table(name = "login_auditoria", schema = "auth")
@AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "login_auditoria_id", updatable = false, nullable = false)),
    @AttributeOverride(name = "creadoEn", column = @Column(name = "creado_en", updatable = false, nullable = false)),
    @AttributeOverride(name = "actualizadoEn", column = @Column(name = "actualizado_en", insertable = false, updatable = false, nullable = true))
})
public class LoginAuditoriaJpaEntity extends BaseJpaEntity {

    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Column(name = "username_intento", length = 150)
    private String usernameIntento;

    @Column(name = "ip_origen", columnDefinition = "INET")
    private String ipOrigen;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "exitoso", nullable = false)
    private boolean exitoso;

    @Column(name = "motivo_fallo")
    private String motivoFallo;

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }
    public String getUsernameIntento() { return usernameIntento; }
    public void setUsernameIntento(String usernameIntento) { this.usernameIntento = usernameIntento; }
    public String getIpOrigen() { return ipOrigen; }
    public void setIpOrigen(String ipOrigen) { this.ipOrigen = ipOrigen; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public boolean isExitoso() { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }
    public String getMotivoFallo() { return motivoFallo; }
    public void setMotivoFallo(String motivoFallo) { this.motivoFallo = motivoFallo; }
}
