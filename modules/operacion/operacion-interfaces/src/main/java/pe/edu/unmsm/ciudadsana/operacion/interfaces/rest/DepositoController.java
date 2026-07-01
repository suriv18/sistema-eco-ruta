package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarDepositoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarDepositoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.DesactivarDepositoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarDepositosUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerDepositoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarDepositoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarDepositosQuery;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerDepositoQuery;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.RegistrarDepositoRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

@Tag(name = "Depósitos", description = "Gestión de depósitos y centros de transferencia")
@RestController
@RequestMapping("/api/v1/operacion/depositos")
public class DepositoController {

    private final RegistrarDepositoUseCase registrarUseCase;
    private final ObtenerDepositoUseCase obtenerUseCase;
    private final ListarDepositosUseCase listarUseCase;
    private final DesactivarDepositoUseCase desactivarUseCase;
    private final CurrentUserProvider currentUser;

    public DepositoController(RegistrarDepositoUseCase registrarUseCase, ObtenerDepositoUseCase obtenerUseCase,
                               ListarDepositosUseCase listarUseCase, DesactivarDepositoUseCase desactivarUseCase,
                               CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.desactivarUseCase = desactivarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Obtener depósito")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ANALISTA')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepositoResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerDepositoQuery(id, user.tenantId())));
    }

    @Operation(summary = "Registrar depósito")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<DepositoResponseDto>> registrar(@Valid @RequestBody RegistrarDepositoRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarDepositoCommand(user.tenantId(), req.distritoId(), req.nombre(), req.tipo());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Listar depósitos")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ANALISTA')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<DepositoResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarDepositosQuery(user.tenantId(), page, size)));
    }

    @Operation(summary = "Desactivar depósito")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(desactivarUseCase.desactivar(new DesactivarDepositoCommand(id, user.tenantId())));
    }
}
