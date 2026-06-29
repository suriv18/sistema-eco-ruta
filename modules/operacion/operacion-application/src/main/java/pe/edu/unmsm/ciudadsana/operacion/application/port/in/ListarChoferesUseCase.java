package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarChoferesQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ListarChoferesUseCase {
    Result<PageResult<ChoferResponseDto>> listar(ListarChoferesQuery query);
}
