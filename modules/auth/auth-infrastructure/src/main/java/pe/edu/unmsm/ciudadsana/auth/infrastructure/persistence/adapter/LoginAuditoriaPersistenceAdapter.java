package pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.adapter;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.LoginAuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.entity.LoginAuditoriaJpaEntity;
import pe.edu.unmsm.ciudadsana.auth.infrastructure.persistence.repository.LoginAuditoriaJpaRepository;

import java.util.UUID;

@Component
public class LoginAuditoriaPersistenceAdapter implements LoginAuditoriaPersistencePort {

    private final LoginAuditoriaJpaRepository repo;

    public LoginAuditoriaPersistenceAdapter(LoginAuditoriaJpaRepository repo) {
        this.repo = repo;
    }

    @Override
    public void registrar(UUID usuarioId, String usernameIntento, String ipOrigen,
                          String userAgent, boolean exitoso, String motivoFallo) {
        LoginAuditoriaJpaEntity entity = new LoginAuditoriaJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setUsuarioId(usuarioId);
        entity.setUsernameIntento(usernameIntento);
        entity.setIpOrigen(ipOrigen);
        entity.setUserAgent(userAgent);
        entity.setExitoso(exitoso);
        entity.setMotivoFallo(motivoFallo);
        repo.save(entity);
    }
}
