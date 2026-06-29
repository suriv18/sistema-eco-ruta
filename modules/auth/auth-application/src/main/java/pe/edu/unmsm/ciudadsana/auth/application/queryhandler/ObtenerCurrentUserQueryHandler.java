package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ObtenerCurrentUserUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerCurrentUserQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObtenerCurrentUserQueryHandler implements ObtenerCurrentUserUseCase {

    private final UsuarioPersistencePort usuarioPersistence;

    public ObtenerCurrentUserQueryHandler(UsuarioPersistencePort usuarioPersistence) {
        this.usuarioPersistence = usuarioPersistence;
    }

    @Override
    public Result<UsuarioResponseDto> obtenerCurrentUser(ObtenerCurrentUserQuery query) {
        Optional<Usuario> optional = usuarioPersistence.findById(UsuarioId.of(query.usuarioId()));
        if (optional.isEmpty()) {
            return Result.failure(ErrorCode.USUARIO_NO_ENCONTRADO);
        }

        Usuario usuario = optional.get();
        if (!usuario.getTenantId().value().equals(query.tenantId())) {
            return Result.failure(ErrorCode.USUARIO_SIN_PERMISO);
        }

        return Result.success(toDto(usuario));
    }

    private UsuarioResponseDto toDto(Usuario u) {
        Set<String> roles = u.getRoles().stream()
                .map(r -> r.value().toString())
                .collect(Collectors.toSet());
        return new UsuarioResponseDto(
                u.getId().value(), u.getTenantId().value(),
                u.getNombresCompletos().nombres(), u.getNombresCompletos().apellidos(),
                u.getEmail().value(), u.getUsername().value(),
                u.getTelefono(), u.getEstado().name(), roles, u.getCreadoEn()
        );
    }
}
