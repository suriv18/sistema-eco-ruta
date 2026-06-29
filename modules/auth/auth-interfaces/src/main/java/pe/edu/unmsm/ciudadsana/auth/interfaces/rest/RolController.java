package pe.edu.unmsm.ciudadsana.auth.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.unmsm.ciudadsana.auth.application.command.ActualizarRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.command.CrearRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.RolResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ActualizarRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.CrearRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.DesactivarRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ListarRolesUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ObtenerRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarRolesQuery;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerRolQuery;
import pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request.ActualizarRolRequest;
import pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request.CrearRolRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Roles", description = "Gestión de roles del sistema")
@RestController
@RequestMapping("/api/v1/roles")
public class RolController {

    private final CrearRolUseCase crearUseCase;
    private final ActualizarRolUseCase actualizarUseCase;
    private final ListarRolesUseCase listarUseCase;
    private final ObtenerRolUseCase obtenerUseCase;
    private final DesactivarRolUseCase desactivarUseCase;

    public RolController(
            CrearRolUseCase crearUseCase,
            ActualizarRolUseCase actualizarUseCase,
            ListarRolesUseCase listarUseCase,
            ObtenerRolUseCase obtenerUseCase,
            DesactivarRolUseCase desactivarUseCase
    ) {
        this.crearUseCase = crearUseCase;
        this.actualizarUseCase = actualizarUseCase;
        this.listarUseCase = listarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.desactivarUseCase = desactivarUseCase;
    }

    @Operation(summary = "Crear rol")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<RolResponseDto>> crear(
            @Valid @RequestBody CrearRolRequest request
    ) {
        CrearRolCommand command = new CrearRolCommand(request.codigo(), request.nombre(), request.descripcion());
        return ResultResponseMapper.toCreated(crearUseCase.crear(command));
    }

    @Operation(summary = "Listar roles")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<RolResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ListarRolesQuery query = new ListarRolesQuery(page, size);
        return ResultResponseMapper.toOk(listarUseCase.listar(query));
    }

    @Operation(summary = "Obtener rol por ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{rolId}")
    public ResponseEntity<ApiResponse<RolResponseDto>> obtener(@PathVariable UUID rolId) {
        ObtenerRolQuery query = new ObtenerRolQuery(rolId);
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(query));
    }

    @Operation(summary = "Actualizar rol")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{rolId}")
    public ResponseEntity<ApiResponse<RolResponseDto>> actualizar(
            @PathVariable UUID rolId,
            @Valid @RequestBody ActualizarRolRequest request
    ) {
        ActualizarRolCommand command = new ActualizarRolCommand(rolId, request.nombre(), request.descripcion());
        return ResultResponseMapper.toOk(actualizarUseCase.actualizar(command));
    }

    @Operation(summary = "Desactivar rol")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{rolId}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable UUID rolId) {
        return ResultResponseMapper.toNoContent(desactivarUseCase.desactivar(rolId));
    }
}
