package pe.edu.unmsm.ciudadsana.integracion.application.port.out;

import pe.edu.unmsm.ciudadsana.integracion.application.dto.RespuestaOptimizacionDto;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.SolicitudOptimizacionDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface OptimizationClientPort {

    Result<RespuestaOptimizacionDto> optimizar(SolicitudOptimizacionDto solicitud);
}
