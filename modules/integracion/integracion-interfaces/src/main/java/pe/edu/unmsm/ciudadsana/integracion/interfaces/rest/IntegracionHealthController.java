package pe.edu.unmsm.ciudadsana.integracion.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.unmsm.ciudadsana.integracion.application.port.out.OefaClientPort;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/integracion")
public class IntegracionHealthController {

    private final OefaClientPort oefaClientPort;

    public IntegracionHealthController(OefaClientPort oefaClientPort) {
        this.oefaClientPort = oefaClientPort;
    }

    @GetMapping("/health")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<?> health() {
        Result<Boolean> oefaOk = oefaClientPort.verificarDisponibilidad();
        Map<String, Object> status = Map.of(
                "optimization", "UP (no health-check en MVP)",
                "osrm", "UP (no health-check en MVP)",
                "oefa", oefaOk.isSuccess() && Boolean.TRUE.equals(oefaOk.getValue()) ? "UP" : "DOWN"
        );
        return ResultResponseMapper.toOk(Result.success(status));
    }
}
