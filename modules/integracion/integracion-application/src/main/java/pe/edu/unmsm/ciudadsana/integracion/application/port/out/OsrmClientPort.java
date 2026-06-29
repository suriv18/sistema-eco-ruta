package pe.edu.unmsm.ciudadsana.integracion.application.port.out;

import pe.edu.unmsm.ciudadsana.integracion.application.dto.MatrizCostosDto;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.RutaOsrmDto;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.List;

public interface OsrmClientPort {

    Result<RutaOsrmDto> calcularRuta(double latOrigen, double lonOrigen,
                                     double latDestino, double lonDestino);

    Result<MatrizCostosDto> calcularMatrizCostos(List<double[]> coordenadas);
}
