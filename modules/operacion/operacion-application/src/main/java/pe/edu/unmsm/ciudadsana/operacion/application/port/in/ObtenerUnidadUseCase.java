package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerUnidadQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ObtenerUnidadUseCase {
    Result<UnidadResponseDto> obtener(ObtenerUnidadQuery query);
}
