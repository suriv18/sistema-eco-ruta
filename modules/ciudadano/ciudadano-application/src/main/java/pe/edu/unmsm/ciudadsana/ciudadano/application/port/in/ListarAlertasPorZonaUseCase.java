package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarAlertasPorZonaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarAlertasPorZonaUseCase {
    Result<PageResult<AlertaResponseDto>> listarPorZona(ListarAlertasPorZonaQuery query);
}
