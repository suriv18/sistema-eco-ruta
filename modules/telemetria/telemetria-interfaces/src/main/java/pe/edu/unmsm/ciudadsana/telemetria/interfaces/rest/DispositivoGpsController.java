package pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarDispositivoCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DispositivoResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarDispositivosUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ObtenerDispositivoUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.RegistrarDispositivoUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarDispositivosQuery;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ObtenerDispositivoQuery;
import pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest.request.RegistrarDispositivoRequest;

import java.util.UUID;

@Tag(name = "Dispositivos GPS", description = "Gestión de dispositivos GPS vinculados a unidades")
@RestController
@RequestMapping("/api/v1/telemetria/dispositivos")
public class DispositivoGpsController {

    private final RegistrarDispositivoUseCase registrarUseCase;
    private final ObtenerDispositivoUseCase obtenerUseCase;
    private final ListarDispositivosUseCase listarUseCase;
    private final CurrentUserProvider currentUser;

    public DispositivoGpsController(RegistrarDispositivoUseCase registrarUseCase,
                                    ObtenerDispositivoUseCase obtenerUseCase,
                                    ListarDispositivosUseCase listarUseCase,
                                    CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar dispositivo GPS")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<DispositivoResponseDto>> registrar(
            @Valid @RequestBody RegistrarDispositivoRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarDispositivoCommand(user.tenantId(), req.unidadExternoId(), req.imei(), req.proveedor());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Obtener dispositivo GPS")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DispositivoResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerDispositivoQuery(id, user.tenantId())));
    }

    @Operation(summary = "Listar dispositivos GPS")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<DispositivoResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarDispositivosQuery(user.tenantId(), page, size)));
    }
}
