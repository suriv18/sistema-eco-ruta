package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarDistritosQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ListarDistritosUseCase {
    Result<PageResult<DistritoResponseDto>> listar(ListarDistritosQuery query);
}
