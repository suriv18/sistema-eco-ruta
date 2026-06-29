package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DispositivoResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarDispositivosQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

public interface ListarDispositivosUseCase {
    Result<PageResult<DispositivoResponseDto>> listar(ListarDispositivosQuery query);
}
