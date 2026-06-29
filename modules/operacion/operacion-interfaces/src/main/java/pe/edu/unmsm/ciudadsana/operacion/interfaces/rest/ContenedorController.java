package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoContenedorCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarContenedorCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ContenedorResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.CambiarEstadoContenedorUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarContenedoresUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarContenedorUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarContenedoresQuery;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.CambiarEstadoContenedorRequest;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.RegistrarContenedorRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Contenedores", description = "Gestión de contenedores de residuos")
@RestController
@RequestMapping("/api/v1/operacion/contenedores")
public class ContenedorController {

    private final RegistrarContenedorUseCase registrarUseCase;
    private final ListarContenedoresUseCase listarUseCase;
    private final CambiarEstadoContenedorUseCase cambiarEstadoUseCase;
    private final CurrentUserProvider currentUser;

    public ContenedorController(RegistrarContenedorUseCase registrarUseCase, ListarContenedoresUseCase listarUseCase,
                                 CambiarEstadoContenedorUseCase cambiarEstadoUseCase, CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.listarUseCase = listarUseCase;
        this.cambiarEstadoUseCase = cambiarEstadoUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar contenedor")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<ContenedorResponseDto>> registrar(@Valid @RequestBody RegistrarContenedorRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarContenedorCommand(user.tenantId(), req.zonaId(), req.codigo(), req.capacidadM3());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Listar contenedores")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<ContenedorResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarContenedoresQuery(user.tenantId(), page, size)));
    }

    @Operation(summary = "Cambiar estado de contenedor")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable UUID id,
                                                            @Valid @RequestBody CambiarEstadoContenedorRequest req) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(cambiarEstadoUseCase.cambiarEstado(new CambiarEstadoContenedorCommand(id, user.tenantId(), req.nuevoEstado())));
    }
}
