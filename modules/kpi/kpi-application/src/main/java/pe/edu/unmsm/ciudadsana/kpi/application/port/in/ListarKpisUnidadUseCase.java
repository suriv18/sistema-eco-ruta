package pe.edu.unmsm.ciudadsana.kpi.application.port.in;

import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiUnidadDto;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisUnidadQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarKpisUnidadUseCase {
    Result<PageResult<KpiUnidadDto>> listar(ListarKpisUnidadQuery query);
}
