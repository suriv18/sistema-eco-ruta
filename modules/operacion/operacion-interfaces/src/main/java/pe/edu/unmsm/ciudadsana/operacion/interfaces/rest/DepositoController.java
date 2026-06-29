package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarDepositoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.DepositoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarDepositosUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarDepositoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarDepositosQuery;
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
    private final ListarDepositosUseCase listarUseCase;
    private final CurrentUserProvider currentUser;

    public DepositoController(RegistrarDepositoUseCase registrarUseCase, ListarDepositosUseCase listarUseCase,
                               CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.listarUseCase = listarUseCase;
        this.currentUser = currentUser;
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
}
