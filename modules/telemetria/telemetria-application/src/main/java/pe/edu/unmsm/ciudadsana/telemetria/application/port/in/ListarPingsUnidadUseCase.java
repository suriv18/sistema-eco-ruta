package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.dto.PingGpsResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarPingsUnidadQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

public interface ListarPingsUnidadUseCase {
    Result<PageResult<PingGpsResponseDto>> listar(ListarPingsUnidadQuery query);
}
