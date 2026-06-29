package pe.edu.unmsm.ciudadsana.ciudadano.application.port.in;

import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarCiudadanosQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarCiudadanosUseCase {
    Result<PageResult<CiudadanoResponseDto>> listar(ListarCiudadanosQuery query);
}
