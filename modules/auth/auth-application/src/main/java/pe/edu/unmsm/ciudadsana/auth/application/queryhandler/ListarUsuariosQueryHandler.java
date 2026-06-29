package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ListarUsuariosUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.UsuarioPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarUsuariosQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Usuario;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ListarUsuariosQueryHandler implements ListarUsuariosUseCase {

    private final UsuarioPersistencePort usuarioPersistence;

    public ListarUsuariosQueryHandler(UsuarioPersistencePort usuarioPersistence) {
        this.usuarioPersistence = usuarioPersistence;
    }

    @Override
    public Result<PageResult<UsuarioResponseDto>> listar(ListarUsuariosQuery query) {
        TenantId tenantId = TenantId.of(query.tenantId());
        PageResult<Usuario> page = usuarioPersistence.findAll(tenantId, query.page(), query.size());
        return Result.success(page.map(this::toDto));
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
