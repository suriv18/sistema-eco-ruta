package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.operacion.application.command.ActivarDistritoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarDistritoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarDistritoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DistritoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ActivarDistritoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.DesactivarDistritoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarDistritosUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerDistritoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarDistritoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarDistritosQuery;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerDistritoQuery;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.RegistrarDistritoRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Distritos", description = "Gestión de distritos operativos")
@RestController
@RequestMapping("/api/v1/operacion/distritos")
public class DistritoController {

    private final RegistrarDistritoUseCase registrarUseCase;
    private final ObtenerDistritoUseCase obtenerUseCase;
    private final ListarDistritosUseCase listarUseCase;
    private final DesactivarDistritoUseCase desactivarUseCase;
    private final ActivarDistritoUseCase activarUseCase;
    private final CurrentUserProvider currentUser;

    public DistritoController(RegistrarDistritoUseCase registrarUseCase,
                               ObtenerDistritoUseCase obtenerUseCase,
                               ListarDistritosUseCase listarUseCase,
                               DesactivarDistritoUseCase desactivarUseCase,
                               ActivarDistritoUseCase activarUseCase,
                               CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.desactivarUseCase = desactivarUseCase;
        this.activarUseCase = activarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar distrito")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<DistritoResponseDto>> registrar(@Valid @RequestBody RegistrarDistritoRequest req) {
        var user = currentUser.requireCurrentUser();
        var command = new RegistrarDistritoCommand(user.tenantId(), req.nombre(), req.ubigeo());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(command));
    }

    @Operation(summary = "Obtener distrito")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ANALISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DistritoResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerDistritoQuery(id, user.tenantId())));
    }

    @Operation(summary = "Listar distritos")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ANALISTA')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<DistritoResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarDistritosQuery(user.tenantId(), page, size)));
    }

    @Operation(summary = "Desactivar distrito")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(desactivarUseCase.desactivar(new DesactivarDistritoCommand(id, user.tenantId())));
    }

    @Operation(summary = "Activar distrito")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(activarUseCase.activar(new ActivarDistritoCommand(id, user.tenantId())));
    }
}
