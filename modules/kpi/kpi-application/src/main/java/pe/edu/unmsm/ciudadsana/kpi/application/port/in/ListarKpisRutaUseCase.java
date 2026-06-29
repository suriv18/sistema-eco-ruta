package pe.edu.unmsm.ciudadsana.kpi.application.port.in;

import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiRutaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisRutaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarKpisRutaUseCase {
    Result<PageResult<KpiRutaDto>> listar(ListarKpisRutaQuery query);
}
