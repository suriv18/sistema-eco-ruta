package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerZonaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ObtenerZonaUseCase {
    Result<ZonaResponseDto> obtener(ObtenerZonaQuery query);
}
