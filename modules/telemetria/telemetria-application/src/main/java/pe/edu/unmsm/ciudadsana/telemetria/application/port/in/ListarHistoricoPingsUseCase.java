package pe.edu.unmsm.ciudadsana.telemetria.application.port.in;

import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.PingGpsResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.HistoricoPingsQuery;

public interface ListarHistoricoPingsUseCase {

    Result<PageResult<PingGpsResponseDto>> listar(HistoricoPingsQuery query);
}
