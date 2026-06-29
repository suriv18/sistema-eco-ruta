package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.operacion.application.command.DesactivarZonaCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarZonaCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.ZonaResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.DesactivarZonaUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ListarZonasUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.ObtenerZonaUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarZonaUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ListarZonasQuery;
import pe.edu.unmsm.ciudadsana.operacion.application.query.ObtenerZonaQuery;
import pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request.RegistrarZonaRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Zonas", description = "Gestión de zonas de recolección")
@RestController
@RequestMapping("/api/v1/operacion/zonas")
public class ZonaController {

    private final RegistrarZonaUseCase registrarUseCase;
    private final ObtenerZonaUseCase obtenerUseCase;
    private final ListarZonasUseCase listarUseCase;
    private final DesactivarZonaUseCase desactivarUseCase;
    private final CurrentUserProvider currentUser;

    public ZonaController(RegistrarZonaUseCase registrarUseCase, ObtenerZonaUseCase obtenerUseCase,
                           ListarZonasUseCase listarUseCase, DesactivarZonaUseCase desactivarUseCase,
                           CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.desactivarUseCase = desactivarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar zona")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<ZonaResponseDto>> registrar(@Valid @RequestBody RegistrarZonaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarZonaCommand(user.tenantId(), req.distritoId(), req.codigo(), req.nombre(), req.tipoZona(), req.prioridad());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Obtener zona")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ANALISTA','OPERADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ZonaResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerZonaQuery(id, user.tenantId())));
    }

    @Operation(summary = "Listar zonas")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ANALISTA','OPERADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<ZonaResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarZonasQuery(user.tenantId(), page, size)));
    }

    @Operation(summary = "Desactivar zona")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ApiResponse<Void>> desactivar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(desactivarUseCase.desactivar(new DesactivarZonaCommand(id, user.tenantId())));
    }
}
