package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarTurnosQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ListarTurnosUseCase {
    Result<PageResult<TurnoResponseDto>> listar(ListarTurnosQuery query);
}
