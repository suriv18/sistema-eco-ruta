package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ListarPermisosUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarPermisosQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Permiso;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarPermisosQueryHandler implements ListarPermisosUseCase {

    private final PermisoPersistencePort permisoPort;

    public ListarPermisosQueryHandler(PermisoPersistencePort permisoPort) {
        this.permisoPort = permisoPort;
    }

    @Override
    public Result<PageResult<PermisoResponseDto>> listar(ListarPermisosQuery query) {
        PageResult<Permiso> page = permisoPort.findAll(query.page(), query.size());
        return Result.success(page.map(PermisoResponseDto::from));
    }
}
