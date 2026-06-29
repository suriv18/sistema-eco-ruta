package pe.edu.unmsm.ciudadsana.integracion.infrastructure.external.osrm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.MatrizCostosDto;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.RutaOsrmDto;
import pe.edu.unmsm.ciudadsana.integracion.application.port.out.OsrmClientPort;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OsrmHttpClientAdapter implements OsrmClientPort {

    private static final Logger log = LoggerFactory.getLogger(OsrmHttpClientAdapter.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public OsrmHttpClientAdapter(
            RestTemplate restTemplate,
            @Value("${integracion.osrm.base-url:http://localhost:5000}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public Result<RutaOsrmDto> calcularRuta(double latOrigen, double lonOrigen,
                                             double latDestino, double lonDestino) {
        try {
            String url = String.format(
                    "%s/route/v1/driving/%f,%f;%f,%f?geometries=geojson&overview=full",
                    baseUrl, lonOrigen, latOrigen, lonDestino, latDestino
            );

            OsrmRouteResponse response = restTemplate.getForObject(url, OsrmRouteResponse.class);

            if (response == null || !"Ok".equals(response.code())
                    || response.routes() == null || response.routes().isEmpty()) {
                return Result.failure(ErrorCode.OSRM_NO_DISPONIBLE, "Respuesta inválida de OSRM");
            }

            OsrmRouteResponse.Route route = response.routes().get(0);
            List<double[]> coords = route.geometry() != null && route.geometry().coordinates() != null
                    ? route.geometry().coordinates().stream()
                            .map(c -> new double[]{c.get(1), c.get(0)})
                            .toList()
                    : List.of();

            return Result.success(new RutaOsrmDto(route.distance(), route.duration(), coords));

        } catch (ResourceAccessException e) {
            log.error("OSRM no disponible: {}", e.getMessage());
            return Result.failure(ErrorCode.OSRM_NO_DISPONIBLE, "No se pudo conectar al servicio OSRM");
        } catch (Exception e) {
            log.error("Error al llamar a OSRM", e);
            return Result.failure(ErrorCode.OSRM_NO_DISPONIBLE, "Error inesperado al llamar a OSRM: " + e.getMessage());
        }
    }

    @Override
    public Result<MatrizCostosDto> calcularMatrizCostos(List<double[]> coordenadas) {
        try {
            String coords = coordenadas.stream()
                    .map(c -> c[1] + "," + c[0])
                    .collect(Collectors.joining(";"));

            String url = String.format(
                    "%s/table/v1/driving/%s?annotations=duration,distance",
                    baseUrl, coords
            );

            OsrmTableResponse response = restTemplate.getForObject(url, OsrmTableResponse.class);

            if (response == null || !"Ok".equals(response.code())) {
                return Result.failure(ErrorCode.OSRM_NO_DISPONIBLE, "Respuesta inválida de OSRM table");
            }

            return Result.success(new MatrizCostosDto(response.durations(), response.distances()));

        } catch (ResourceAccessException e) {
            log.error("OSRM no disponible al calcular matriz: {}", e.getMessage());
            return Result.failure(ErrorCode.OSRM_NO_DISPONIBLE, "No se pudo conectar al servicio OSRM");
        } catch (Exception e) {
            log.error("Error al calcular matriz OSRM", e);
            return Result.failure(ErrorCode.OSRM_NO_DISPONIBLE, "Error inesperado al calcular matriz: " + e.getMessage());
        }
    }
}
