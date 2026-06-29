package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CrearTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.command.FinalizarTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.command.IniciarTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.CrearTurnoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.FinalizarTurnoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.IniciarTurnoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarTurnosUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerTurnoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarTurnosQuery;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerTurnoQuery;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.CrearTurnoRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Turnos", description = "Gestión de turnos operativos")
@RestController
@RequestMapping("/api/v1/operacion/turnos")
public class TurnoController {

    private final CrearTurnoUseCase crearUseCase;
    private final ObtenerTurnoUseCase obtenerUseCase;
    private final ListarTurnosUseCase listarUseCase;
    private final IniciarTurnoUseCase iniciarUseCase;
    private final FinalizarTurnoUseCase finalizarUseCase;
    private final CurrentUserProvider currentUser;

    public TurnoController(CrearTurnoUseCase crearUseCase, ObtenerTurnoUseCase obtenerUseCase,
                            ListarTurnosUseCase listarUseCase, IniciarTurnoUseCase iniciarUseCase,
                            FinalizarTurnoUseCase finalizarUseCase, CurrentUserProvider currentUser) {
        this.crearUseCase = crearUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.iniciarUseCase = iniciarUseCase;
        this.finalizarUseCase = finalizarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Crear turno")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<TurnoResponseDto>> crear(@Valid @RequestBody CrearTurnoRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new CrearTurnoCommand(user.tenantId(), req.unidadId(), req.choferId(), req.distritoId(),
                req.fecha(), req.horaInicio(), req.horaFin(), req.tipoTurno());
        return ResultResponseMapper.toCreated(crearUseCase.crear(cmd));
    }

    @Operation(summary = "Obtener turno")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TurnoResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerTurnoQuery(id, user.tenantId())));
    }

    @Operation(summary = "Listar turnos")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<TurnoResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarTurnosQuery(user.tenantId(), page, size)));
    }

    @Operation(summary = "Iniciar turno")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<ApiResponse<Void>> iniciar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(iniciarUseCase.iniciar(new IniciarTurnoCommand(id, user.tenantId())));
    }

    @Operation(summary = "Finalizar turno")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<ApiResponse<Void>> finalizar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(finalizarUseCase.finalizar(new FinalizarTurnoCommand(id, user.tenantId())));
    }
}
