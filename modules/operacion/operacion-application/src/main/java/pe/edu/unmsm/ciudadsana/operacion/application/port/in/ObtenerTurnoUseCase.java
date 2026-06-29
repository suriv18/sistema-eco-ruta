package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerTurnoQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ObtenerTurnoUseCase {
    Result<TurnoResponseDto> obtener(ObtenerTurnoQuery query);
}
