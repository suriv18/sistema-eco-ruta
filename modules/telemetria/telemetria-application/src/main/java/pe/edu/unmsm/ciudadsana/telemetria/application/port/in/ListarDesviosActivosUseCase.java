package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DesvioRutaResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarDesviosActivosQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

public interface ListarDesviosActivosUseCase {
    Result<PageResult<DesvioRutaResponseDto>> listar(ListarDesviosActivosQuery query);
}
