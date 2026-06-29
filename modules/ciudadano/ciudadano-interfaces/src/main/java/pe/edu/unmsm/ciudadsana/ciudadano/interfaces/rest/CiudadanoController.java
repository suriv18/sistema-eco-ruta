package pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.RegistrarCiudadanoCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.CiudadanoResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ListarCiudadanosUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ObtenerCiudadanoUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.RegistrarCiudadanoUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarCiudadanosQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ObtenerCiudadanoQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request.RegistrarCiudadanoRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Ciudadanos", description = "Gestión de ciudadanos registrados")
@RestController
@RequestMapping("/api/v1/ciudadanos")
public class CiudadanoController {

    private final RegistrarCiudadanoUseCase registrarUseCase;
    private final ObtenerCiudadanoUseCase obtenerUseCase;
    private final ListarCiudadanosUseCase listarUseCase;
    private final CurrentUserProvider currentUser;

    public CiudadanoController(RegistrarCiudadanoUseCase registrarUseCase,
                                ObtenerCiudadanoUseCase obtenerUseCase,
                                ListarCiudadanosUseCase listarUseCase,
                                CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar ciudadano")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CiudadanoResponseDto>> registrar(@Valid @RequestBody RegistrarCiudadanoRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarCiudadanoCommand(user.tenantId(), req.nombres(), req.apellidos(), req.email(), req.telefono(), req.documento());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Obtener ciudadano")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CiudadanoResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerCiudadanoQuery(id, user.tenantId())));
    }

    @Operation(summary = "Listar ciudadanos")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<CiudadanoResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarCiudadanosQuery(user.tenantId(), page, size)));
    }
}
