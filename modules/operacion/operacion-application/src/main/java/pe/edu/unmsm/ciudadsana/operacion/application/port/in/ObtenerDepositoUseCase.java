package pe.edu.unmsm.ciudadsana.operacion.application.port.in;

import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerDepositoQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerDepositoUseCase {
    Result<DepositoResponseDto> obtener(ObtenerDepositoQuery query);
}
