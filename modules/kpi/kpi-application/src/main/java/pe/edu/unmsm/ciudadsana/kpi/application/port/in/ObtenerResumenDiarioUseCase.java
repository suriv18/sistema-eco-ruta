package pe.edu.unmsm.ciudadsana.kpi.application.port.in;

import pe.edu.unmsm.ciudadsana.kpi.application.dto.ResumenOperativoDto;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ObtenerResumenDiarioQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerResumenDiarioUseCase {
    Result<ResumenOperativoDto> obtener(ObtenerResumenDiarioQuery query);
}
