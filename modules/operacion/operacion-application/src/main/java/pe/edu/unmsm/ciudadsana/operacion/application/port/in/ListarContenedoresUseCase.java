package pe.edu.unmsm.ciudadsana.operacion.application.port.in;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarContenedoresQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
public interface ListarContenedoresUseCase {
    Result<PageResult<ContenedorResponseDto>> listar(ListarContenedoresQuery query);
}
