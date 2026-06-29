package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarUnidadesQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ListarUnidadesUseCase {
    Result<PageResult<UnidadResponseDto>> listar(ListarUnidadesQuery query);
}
