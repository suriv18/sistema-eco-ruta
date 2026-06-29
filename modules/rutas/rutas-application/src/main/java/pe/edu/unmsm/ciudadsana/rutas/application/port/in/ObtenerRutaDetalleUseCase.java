package pe.edu.unmsm.ciudadsana.rutas.application.port.in;

import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaDetalleResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ObtenerRutaDetalleQuery;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

public interface ObtenerRutaDetalleUseCase {
    Result<RutaDetalleResponseDto> obtenerDetalle(ObtenerRutaDetalleQuery query);
}
