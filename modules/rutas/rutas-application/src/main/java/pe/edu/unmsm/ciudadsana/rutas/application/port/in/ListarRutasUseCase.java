package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ListarRutasQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarRutasUseCase {
    Result<PageResult<RutaResponseDto>> listar(ListarRutasQuery query);
}
