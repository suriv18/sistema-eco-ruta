package pe.edu.unmsm.ciudadsana.kpi.application.port.in;

import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiAlertaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisAlertaQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ListarKpisAlertaUseCase {
    Result<PageResult<KpiAlertaDto>> listar(ListarKpisAlertaQuery query);
}
