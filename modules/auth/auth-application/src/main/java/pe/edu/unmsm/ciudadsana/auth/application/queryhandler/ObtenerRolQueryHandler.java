package pe.edu.unmsm.ciudadsana.auth.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ObtenerRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.RolPersistencePort;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerRolQuery;
import pe.edu.unmsm.ciudadsana.auth.domain.model.Rol;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RolId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.Optional;

@Component
public class ObtenerRolQueryHandler implements ObtenerRolUseCase {

    private final RolPersistencePort rolPort;

    public ObtenerRolQueryHandler(RolPersistencePort rolPort) {
        this.rolPort = rolPort;
    }

    @Override
    public Result<RolResponseDto> obtener(ObtenerRolQuery query) {
        Optional<Rol> rolOpt = rolPort.findById(RolId.of(query.rolId()));
        if (rolOpt.isEmpty()) {
            return Result.failure(ErrorCode.ROL_NO_ENCONTRADO);
        }
        return Result.success(RolResponseDto.from(rolOpt.get()));
    }
}
