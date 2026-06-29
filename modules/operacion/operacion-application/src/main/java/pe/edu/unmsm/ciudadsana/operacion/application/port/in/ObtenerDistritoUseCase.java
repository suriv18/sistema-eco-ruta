package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerDistritoQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ObtenerDistritoUseCase {
    Result<DistritoResponseDto> obtener(ObtenerDistritoQuery query);
}
