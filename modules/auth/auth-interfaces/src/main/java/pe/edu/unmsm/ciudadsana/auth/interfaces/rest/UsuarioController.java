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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.unmsm.ciudadsana.auth.application.command.ActivarUsuarioCommand;
import pe.edu.unmsm.ciudadsana.auth.application.command.AsignarRolCommand;
import pe.edu.unmsm.ciudadsana.auth.application.command.BloquearUsuarioCommand;
import pe.edu.unmsm.ciudadsana.auth.application.command.RegistrarUsuarioCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ActivarUsuarioUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.AsignarRolUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.BloquearUsuarioUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ListarUsuariosUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.RegistrarUsuarioUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.query.ListarUsuariosQuery;
import pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request.AsignarRolRequest;
import pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request.RegistrarUsuarioRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.security.model.AuthenticatedUser;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Usuarios", description = "Gestión de usuarios")
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final RegistrarUsuarioUseCase registrarUseCase;
    private final BloquearUsuarioUseCase bloquearUseCase;
    private final ActivarUsuarioUseCase activarUseCase;
    private final AsignarRolUseCase asignarRolUseCase;
    private final ListarUsuariosUseCase listarUseCase;
    private final CurrentUserProvider currentUserProvider;

    public UsuarioController(
            RegistrarUsuarioUseCase registrarUseCase,
            BloquearUsuarioUseCase bloquearUseCase,
            ActivarUsuarioUseCase activarUseCase,
            AsignarRolUseCase asignarRolUseCase,
            ListarUsuariosUseCase listarUseCase,
            CurrentUserProvider currentUserProvider
    ) {
        this.registrarUseCase = registrarUseCase;
        this.bloquearUseCase = bloquearUseCase;
        this.activarUseCase = activarUseCase;
        this.asignarRolUseCase = asignarRolUseCase;
        this.listarUseCase = listarUseCase;
        this.currentUserProvider = currentUserProvider;
    }

    @Operation(summary = "Registrar nuevo usuario")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> registrar(
            @Valid @RequestBody RegistrarUsuarioRequest request
    ) {
        RegistrarUsuarioCommand command = new RegistrarUsuarioCommand(
                request.tenantId(), request.nombres(), request.apellidos(),
                request.email(), request.username(), request.password(), request.telefono()
        );
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(command));
    }

    @Operation(summary = "Listar usuarios del tenant")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<UsuarioResponseDto>>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        AuthenticatedUser user = currentUserProvider.requireCurrentUser();
        ListarUsuariosQuery query = new ListarUsuariosQuery(user.tenantId(), page, size);
        return ResultResponseMapper.toOk(listarUseCase.listar(query));
    }

    @Operation(summary = "Bloquear usuario")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{usuarioId}/bloquear")
    public ResponseEntity<ApiResponse<Void>> bloquear(@PathVariable UUID usuarioId) {
        AuthenticatedUser user = currentUserProvider.requireCurrentUser();
        BloquearUsuarioCommand command = new BloquearUsuarioCommand(usuarioId, user.tenantId());
        return ResultResponseMapper.toNoContent(bloquearUseCase.bloquear(command));
    }

    @Operation(summary = "Activar usuario")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{usuarioId}/activar")
    public ResponseEntity<ApiResponse<Void>> activar(@PathVariable UUID usuarioId) {
        AuthenticatedUser user = currentUserProvider.requireCurrentUser();
        ActivarUsuarioCommand command = new ActivarUsuarioCommand(usuarioId, user.tenantId());
        return ResultResponseMapper.toNoContent(activarUseCase.activar(command));
    }

    @Operation(summary = "Asignar rol a usuario")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{usuarioId}/roles")
    public ResponseEntity<ApiResponse<Void>> asignarRol(
            @PathVariable UUID usuarioId,
            @Valid @RequestBody AsignarRolRequest request
    ) {
        AuthenticatedUser user = currentUserProvider.requireCurrentUser();
        AsignarRolCommand command = new AsignarRolCommand(usuarioId, request.rolId(), user.tenantId());
        return ResultResponseMapper.toNoContent(asignarRolUseCase.asignarRol(command));
    }
}
