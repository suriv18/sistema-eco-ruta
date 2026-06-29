package pe.edu.unmsm.ciudadsana.kpi.application.port.in;

import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiZonaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisZonaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarKpisZonaUseCase {
    Result<PageResult<KpiZonaDto>> listar(ListarKpisZonaQuery query);
}
