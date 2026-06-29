package pe.edu.unmsm.ciudadsana.integracion.infrastructure.external.optimization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.RespuestaOptimizacionDto;
import pe.edu.unmsm.ciudadsana.integracion.application.dto.SolicitudOptimizacionDto;
import pe.edu.unmsm.ciudadsana.integracion.application.port.out.OptimizationClientPort;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class FastApiOptimizationClientAdapter implements OptimizationClientPort {

    private static final Logger log = LoggerFactory.getLogger(FastApiOptimizationClientAdapter.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public FastApiOptimizationClientAdapter(
            RestTemplate restTemplate,
            @Value("${integracion.optimization.base-url:http://localhost:8001}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public Result<RespuestaOptimizacionDto> optimizar(SolicitudOptimizacionDto solicitud) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<OptimizationRequest> request =
                    new HttpEntity<>(OptimizationRequest.from(solicitud), headers);

            ResponseEntity<OptimizationResponse> response = restTemplate.postForEntity(
                    baseUrl + "/api/v1/optimizar",
                    request,
                    OptimizationResponse.class
            );

            if (response.getBody() == null) {
                return Result.failure(ErrorCode.OPTIMIZADOR_NO_DISPONIBLE,
                        "Respuesta vacía del servicio de optimización");
            }

            OptimizationResponse body = response.getBody();
            if ("NO_FACTIBLE".equals(body.estado())) {
                return Result.failure(ErrorCode.SOLUCION_NO_FACTIBLE, body.mensaje());
            }

            return Result.success(body.toDto());

        } catch (ResourceAccessException e) {
            log.error("Servicio de optimización no disponible: {}", e.getMessage());
            return Result.failure(ErrorCode.OPTIMIZADOR_NO_DISPONIBLE,
                    "No se pudo conectar al servicio de optimización");
        } catch (Exception e) {
            log.error("Error al llamar al servicio de optimización", e);
            return Result.failure(ErrorCode.OPTIMIZADOR_NO_DISPONIBLE,
                    "Error inesperado al llamar al servicio de optimización: " + e.getMessage());
        }
    }
}
