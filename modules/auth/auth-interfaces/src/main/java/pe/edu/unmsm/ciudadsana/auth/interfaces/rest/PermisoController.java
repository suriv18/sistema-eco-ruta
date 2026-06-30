package pe.edu.unmsm.ciudadsana.auth.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.unmsm.ciudadsana.auth.application.command.ActualizarPermisoCommand;
import pe.edu.unmsm.ciudadsana.auth.application.command.CrearPermisoCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.PermisoResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ActualizarPermisoUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.CrearPermisoUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ListarPermisosUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ObtenerPermisoUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarPermisosQuery;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerPermisoQuery;
import pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request.ActualizarPermisoRequest;
import pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request.CrearPermisoRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Permisos", description = "Gestión de permisos del sistema")
@RestController
@RequestMapping("/api/v1/permisos")
public class PermisoController {

    private final CrearPermisoUseCase crearUseCase;
    private final ActualizarPermisoUseCase actualizarUseCase;
    private final ListarPermisosUseCase listarUseCase;
    private final ObtenerPermisoUseCase obtenerUseCase;

    public PermisoController(
            CrearPermisoUseCase crearUseCase,
            ActualizarPermisoUseCase actualizarUseCase,
            ListarPermisosUseCase listarUseCase,
            ObtenerPermisoUseCase obtenerUseCase
    ) {
        this.crearUseCase = crearUseCase;
        this.actualizarUseCase = actualizarUseCase;
        this.listarUseCase = listarUseCase;
        this.obtenerUseCase = obtenerUseCase;
    }

    @Operation(summary = "Crear permiso")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<PermisoResponseDto>> crear(
            @Valid @RequestBody CrearPermisoRequest request
    ) {
        CrearPermisoCommand command = new CrearPermisoCommand(request.codigo(), request.modulo(), request.descripcion());
        return ResultResponseMapper.toCreated(crearUseCase.crear(command));
    }

    @Operation(summary = "Listar permisos")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<PermisoResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        ListarPermisosQuery query = new ListarPermisosQuery(page, size);
        return ResultResponseMapper.toOk(listarUseCase.listar(query));
    }

    @Operation(summary = "Obtener permiso por ID")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{permisoId}")
    public ResponseEntity<ApiResponse<PermisoResponseDto>> obtener(@PathVariable UUID permisoId) {
        ObtenerPermisoQuery query = new ObtenerPermisoQuery(permisoId);
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(query));
    }

    @Operation(summary = "Actualizar permiso")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{permisoId}")
    public ResponseEntity<ApiResponse<PermisoResponseDto>> actualizar(
            @PathVariable UUID permisoId,
            @Valid @RequestBody ActualizarPermisoRequest request
    ) {
        ActualizarPermisoCommand command = new ActualizarPermisoCommand(permisoId, request.modulo(), request.descripcion());
        return ResultResponseMapper.toOk(actualizarUseCase.actualizar(command));
    }
}
