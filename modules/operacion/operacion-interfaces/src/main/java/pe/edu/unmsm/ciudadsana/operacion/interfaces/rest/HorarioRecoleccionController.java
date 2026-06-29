package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarHorarioCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarHorariosPorZonaUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerHorarioUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarHorarioUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarHorariosPorZonaQuery;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerHorarioQuery;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.RegistrarHorarioRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Horarios de Recolección", description = "Gestión de horarios de recolección por zona")
@RestController
@RequestMapping("/api/v1/operacion/horarios-recoleccion")
public class HorarioRecoleccionController {

    private final RegistrarHorarioUseCase registrarUseCase;
    private final ObtenerHorarioUseCase obtenerUseCase;
    private final ListarHorariosPorZonaUseCase listarUseCase;
    private final CurrentUserProvider currentUser;

    public HorarioRecoleccionController(RegistrarHorarioUseCase registrarUseCase,
                                         ObtenerHorarioUseCase obtenerUseCase,
                                         ListarHorariosPorZonaUseCase listarUseCase,
                                         CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar horario de recolección")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<HorarioResponseDto>> registrar(@Valid @RequestBody RegistrarHorarioRequest req) {
        var user = currentUser.requireCurrentUser();
        var command = new RegistrarHorarioCommand(user.tenantId(), req.zonaId(), req.diaSemana(), req.horaInicio(), req.horaFin(), req.observacion());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(command));
    }

    @Operation(summary = "Obtener horario de recolección")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ANALISTA','OPERADOR')")
    @GetMapping("/{horarioId}")
    public ResponseEntity<ApiResponse<HorarioResponseDto>> obtener(@PathVariable UUID horarioId) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerHorarioQuery(horarioId, user.tenantId())));
    }

    @Operation(summary = "Listar horarios de recolección por zona")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ANALISTA','OPERADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<HorarioResponseDto>>> listar(
            @RequestParam UUID zonaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarHorariosPorZonaQuery(user.tenantId(), zonaId, page, size)));
    }
}
