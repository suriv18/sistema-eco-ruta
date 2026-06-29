package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ObtenerPermisoUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.PermisoPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerPermisoQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.PermisoId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ObtenerPermisoQueryHandler implements ObtenerPermisoUseCase {

    private final PermisoPersistencePort permisoPort;

    public ObtenerPermisoQueryHandler(PermisoPersistencePort permisoPort) {
        this.permisoPort = permisoPort;
    }

    @Override
    public Result<PermisoResponseDto> obtener(ObtenerPermisoQuery query) {
        return permisoPort.findById(PermisoId.of(query.permisoId()))
                .map(p -> Result.<PermisoResponseDto>success(PermisoResponseDto.from(p)))
                .orElseGet(() -> Result.failure(ErrorCode.PERMISO_NO_ENCONTRADO));
    }
}
