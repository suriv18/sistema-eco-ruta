package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarAlertasQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarAlertasUseCase {
    Result<PageResult<AlertaResponseDto>> listar(ListarAlertasQuery query);
}
