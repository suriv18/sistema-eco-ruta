package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CambiarEstadoUnidadCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarUnidadCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.CambiarEstadoUnidadUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarUnidadesUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerUnidadUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarUnidadUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarUnidadesQuery;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerUnidadQuery;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.CambiarEstadoUnidadRequest;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.RegistrarUnidadRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Unidades", description = "Gestión de unidades de recolección")
@RestController
@RequestMapping("/api/v1/operacion/unidades")
public class UnidadController {

    private final RegistrarUnidadUseCase registrarUseCase;
    private final ObtenerUnidadUseCase obtenerUseCase;
    private final ListarUnidadesUseCase listarUseCase;
    private final CambiarEstadoUnidadUseCase cambiarEstadoUseCase;
    private final CurrentUserProvider currentUser;

    public UnidadController(RegistrarUnidadUseCase registrarUseCase, ObtenerUnidadUseCase obtenerUseCase,
                             ListarUnidadesUseCase listarUseCase, CambiarEstadoUnidadUseCase cambiarEstadoUseCase,
                             CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.cambiarEstadoUseCase = cambiarEstadoUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar unidad")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<UnidadResponseDto>> registrar(@Valid @RequestBody RegistrarUnidadRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarUnidadCommand(user.tenantId(), req.placa(), req.codigoInterno(), req.tipoUnidad(), req.capacidadM3(), req.capacidadKg());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Obtener unidad")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnidadResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerUnidadQuery(id, user.tenantId())));
    }

    @Operation(summary = "Listar unidades")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<UnidadResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarUnidadesQuery(user.tenantId(), page, size)));
    }

    @Operation(summary = "Cambiar estado operativo")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Void>> cambiarEstado(@PathVariable UUID id,
                                                            @Valid @RequestBody CambiarEstadoUnidadRequest req) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(cambiarEstadoUseCase.cambiarEstado(new CambiarEstadoUnidadCommand(id, user.tenantId(), req.nuevoEstado())));
    }
}
