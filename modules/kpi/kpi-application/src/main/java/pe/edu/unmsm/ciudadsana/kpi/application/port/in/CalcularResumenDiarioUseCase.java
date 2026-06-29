package pe.edu.unmsm.ciudadsana.kpi.application.port.in;

import pe.edu.unmsm.ciudadsana.kpi.application.command.CalcularResumenDiarioCommand;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.ResumenOperativoDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface CalcularResumenDiarioUseCase {
    Result<ResumenOperativoDto> calcular(CalcularResumenDiarioCommand command);
}
