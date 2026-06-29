package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarZonasQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ListarZonasUseCase {
    Result<PageResult<ZonaResponseDto>> listar(ListarZonasQuery query);
}
