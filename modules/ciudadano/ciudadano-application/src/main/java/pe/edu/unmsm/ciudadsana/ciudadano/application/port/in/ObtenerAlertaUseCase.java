package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ObtenerAlertaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerAlertaUseCase {
    Result<AlertaResponseDto> obtener(ObtenerAlertaQuery query);
}
