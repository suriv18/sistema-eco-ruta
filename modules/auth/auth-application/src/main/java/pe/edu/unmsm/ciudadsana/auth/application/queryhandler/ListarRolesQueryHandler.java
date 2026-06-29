package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ListarRolesUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarRolesQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarRolesQueryHandler implements ListarRolesUseCase {

    private final RolPersistencePort rolPort;

    public ListarRolesQueryHandler(RolPersistencePort rolPort) {
        this.rolPort = rolPort;
    }

    @Override
    public Result<PageResult<RolResponseDto>> listar(ListarRolesQuery query) {
        PageResult<Rol> page = rolPort.findAll(query.page(), query.size());
        return Result.success(page.map(this::toDto));
    }

    private RolResponseDto toDto(Rol rol) {
        return new RolResponseDto(rol.getId().value(), rol.getCodigo(), rol.getNombre(), rol.getDescripcion(), rol.isActivo());
    }
}
