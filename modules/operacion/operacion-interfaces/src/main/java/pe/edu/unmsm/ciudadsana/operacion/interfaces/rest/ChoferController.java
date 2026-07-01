package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoChoferCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarChoferCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ChoferResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.CambiarEstadoChoferUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarChoferesUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerChoferUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarChoferUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarChoferesQuery;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerChoferQuery;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.CambiarEstadoChoferRequest;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.RegistrarChoferRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

@Tag(name = "Choferes", description = "Gestión de choferes")
@RestController
@RequestMapping("/api/v1/operacion/choferes")
public class ChoferController {

    private final RegistrarChoferUseCase registrarUseCase;
    private final ObtenerChoferUseCase obtenerUseCase;
    private final ListarChoferesUseCase listarUseCase;
    private final CambiarEstadoChoferUseCase cambiarEstadoUseCase;
    private final CurrentUserProvider currentUser;

    public ChoferController(RegistrarChoferUseCase registrarUseCase, ObtenerChoferUseCase obtenerUseCase,
                             ListarChoferesUseCase listarUseCase, CambiarEstadoChoferUseCase cambiarEstadoUseCase,
                             CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.cambiarEstadoUseCase = cambiarEstadoUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Obtener chofer")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChoferResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerChoferQuery(id, user.tenantId())));
    }

    @Operation(summary = "Registrar chofer")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<ChoferResponseDto>> registrar(@Valid @RequestBody RegistrarChoferRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarChoferCommand(user.tenantId(), req.nombres(), req.apellidos(), req.dni(), req.licencia(), req.telefono());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Listar choferes")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<ChoferResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarChoferesQuery(user.tenantId(), page, size)));
    }

    @Operation(summary = "Cambiar estado de chofer")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<ChoferResponseDto>> cambiarEstado(@PathVariable UUID id,
                                                                         @Valid @RequestBody CambiarEstadoChoferRequest req) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(cambiarEstadoUseCase.cambiarEstado(new CambiarEstadoChoferCommand(id, user.tenantId(), req.estado())));
    }
}
