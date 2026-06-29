package pe.edu.unmsm.ciudadsana.auth.application.port.out;

import java.util.UUID;

public interface LoginAuditoriaPersistencePort {
    void registrar(UUID usuarioId, String usernameIntento, String ipOrigen, String userAgent, boolean exitoso, String motivoFallo);
}
