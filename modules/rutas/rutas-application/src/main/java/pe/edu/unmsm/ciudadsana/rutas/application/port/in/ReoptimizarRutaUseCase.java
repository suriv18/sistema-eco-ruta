package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.command.ReoptimizarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ReoptimizarRutaUseCase {
    Result<RutaResponseDto> reoptimizar(ReoptimizarRutaCommand cmd);
}
