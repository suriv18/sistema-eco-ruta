package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EstadoUnidadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarEstadosUnidadesQuery;

public interface ListarEstadosUnidadesUseCase {

    Result<PageResult<EstadoUnidadResponseDto>> listar(ListarEstadosUnidadesQuery query);
}
