package pe.edu.unmsm.ciudadsana.auth.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.unmsm.ciudadsana.auth.application.command.LoginCommand;
import pe.edu.unmsm.ciudadsana.auth.application.command.LogoutCommand;
import pe.edu.unmsm.ciudadsana.auth.application.command.RefreshTokenCommand;
import pe.edu.unmsm.ciudadsana.auth.application.dto.LoginResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.dto.UsuarioResponseDto;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.LoginUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.LogoutUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.ObtenerCurrentUserUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.port.in.RefreshTokenUseCase;
import pe.edu.unmsm.ciudadsana.auth.application.query.ObtenerCurrentUserQuery;
import pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request.LoginRequest;
import pe.edu.unmsm.ciudadsana.auth.interfaces.rest.request.RefreshTokenRequest;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.security.model.AuthenticatedUser;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

@Tag(name = "Auth", description = "Autenticación y autorización")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final ObtenerCurrentUserUseCase currentUserUseCase;
    private final CurrentUserProvider currentUserProvider;

    public AuthController(
            LoginUseCase loginUseCase,
            RefreshTokenUseCase refreshTokenUseCase,
            LogoutUseCase logoutUseCase,
            ObtenerCurrentUserUseCase currentUserUseCase,
            CurrentUserProvider currentUserProvider
    ) {
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.logoutUseCase = logoutUseCase;
        this.currentUserUseCase = currentUserUseCase;
        this.currentUserProvider = currentUserProvider;
    }

    @Operation(summary = "Iniciar sesión")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        String ip = resolveClientIp(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        LoginCommand command = new LoginCommand(request.username(), request.password(), request.tenantId(), ip, userAgent);
        return ResultResponseMapper.toOk(loginUseCase.login(command));
    }

    @Operation(summary = "Renovar token de acceso")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponseDto>> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        return ResultResponseMapper.toOk(refreshTokenUseCase.refresh(new RefreshTokenCommand(request.refreshToken())));
    }

    @Operation(summary = "Cerrar sesión")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        AuthenticatedUser user = currentUserProvider.requireCurrentUser();
        LogoutCommand command = new LogoutCommand(user.usuarioId(), request.refreshToken());
        return ResultResponseMapper.toNoContent(logoutUseCase.logout(command));
    }

    @Operation(summary = "Obtener usuario autenticado")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioResponseDto>> me() {
        AuthenticatedUser user = currentUserProvider.requireCurrentUser();
        ObtenerCurrentUserQuery query = new ObtenerCurrentUserQuery(user.usuarioId(), user.tenantId());
        return ResultResponseMapper.toOk(currentUserUseCase.obtenerCurrentUser(query));
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
