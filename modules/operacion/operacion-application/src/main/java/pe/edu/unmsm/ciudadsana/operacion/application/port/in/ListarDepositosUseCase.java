package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarDepositosQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ListarDepositosUseCase {
    Result<PageResult<DepositoResponseDto>> listar(ListarDepositosQuery query);
}
