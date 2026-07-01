package pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EstadoUnidadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarEstadosUnidadesUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ObtenerEstadoUnidadUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarEstadosUnidadesQuery;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ObtenerEstadoUnidadQuery;

import java.util.UUID;

@Tag(name = "Estado de Unidades", description = "Consulta del estado en tiempo real de las unidades")
@RestController
@RequestMapping("/api/v1/telemetria/estado-unidades")
public class EstadoUnidadController {

    private final ObtenerEstadoUnidadUseCase obtenerUseCase;
    private final ListarEstadosUnidadesUseCase listarUseCase;
    private final CurrentUserProvider currentUser;

    public EstadoUnidadController(ObtenerEstadoUnidadUseCase obtenerUseCase,
                                  ListarEstadosUnidadesUseCase listarUseCase,
                                  CurrentUserProvider currentUser) {
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Listar estado de todas las unidades")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<EstadoUnidadResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarEstadosUnidadesQuery(user.tenantId(), page, size)));
    }

    @Operation(summary = "Obtener estado actual de una unidad")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/{unidadId}")
    public ResponseEntity<ApiResponse<EstadoUnidadResponseDto>> obtener(@PathVariable UUID unidadId) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerEstadoUnidadQuery(unidadId, user.tenantId())));
    }
}
