package pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarEventoConectividadCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.EventoConectividadResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarEventosConectividadUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.RegistrarEventoConectividadUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarEventosConectividadQuery;
import pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest.request.RegistrarEventoConectividadRequest;

import java.util.UUID;

@Tag(name = "Eventos de Conectividad", description = "Registro y consulta de eventos de conectividad de dispositivos")
@RestController
@RequestMapping("/api/v1/telemetria/eventos-conectividad")
public class EventoConectividadController {

    private final RegistrarEventoConectividadUseCase registrarUseCase;
    private final ListarEventosConectividadUseCase listarUseCase;
    private final CurrentUserProvider currentUser;

    public EventoConectividadController(RegistrarEventoConectividadUseCase registrarUseCase,
                                        ListarEventosConectividadUseCase listarUseCase,
                                        CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.listarUseCase = listarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar evento de conectividad")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<EventoConectividadResponseDto>> registrar(
            @Valid @RequestBody RegistrarEventoConectividadRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarEventoConectividadCommand(
                user.tenantId(),
                req.unidadExternoId(),
                req.dispositivoId(),
                req.tipoEvento(),
                req.detalle()
        );
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Listar eventos de conectividad de una unidad")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/unidad/{unidadId}")
    public ResponseEntity<ApiResponse<PageResult<EventoConectividadResponseDto>>> listar(
            @PathVariable UUID unidadId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarEventosConectividadQuery(unidadId, user.tenantId(), page, size)));
    }
}
