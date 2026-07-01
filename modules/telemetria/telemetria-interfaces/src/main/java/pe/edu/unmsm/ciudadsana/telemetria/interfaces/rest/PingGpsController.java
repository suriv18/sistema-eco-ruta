package pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.ProcesarPingGpsCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.PingGpsResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarHistoricoPingsUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarPingsUnidadUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ProcesarPingGpsUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.HistoricoPingsQuery;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarPingsUnidadQuery;
import pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest.request.ProcesarPingGpsRequest;

import java.time.Instant;
import java.util.UUID;

@Tag(name = "Pings GPS", description = "Recepción y consulta de pings GPS de unidades")
@RestController
@RequestMapping("/api/v1/telemetria/pings")
public class PingGpsController {

    private final ProcesarPingGpsUseCase procesarUseCase;
    private final ListarPingsUnidadUseCase listarUseCase;
    private final ListarHistoricoPingsUseCase listarHistoricoUseCase;
    private final CurrentUserProvider currentUser;

    public PingGpsController(ProcesarPingGpsUseCase procesarUseCase,
                             ListarPingsUnidadUseCase listarUseCase,
                             ListarHistoricoPingsUseCase listarHistoricoUseCase,
                             CurrentUserProvider currentUser) {
        this.procesarUseCase = procesarUseCase;
        this.listarUseCase = listarUseCase;
        this.listarHistoricoUseCase = listarHistoricoUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Procesar ping GPS")
    @PreAuthorize("hasAnyRole('ADMIN','OPERADOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<PingGpsResponseDto>> procesar(
            @Valid @RequestBody ProcesarPingGpsRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new ProcesarPingGpsCommand(
                user.tenantId(),
                req.dispositivoId(),
                req.unidadExternoId(),
                req.rutaExternoId(),
                req.ts(),
                req.latitud(),
                req.longitud(),
                req.velocidadKmh(),
                req.rumboGrados(),
                req.precisionM(),
                req.origen()
        );
        return ResultResponseMapper.toCreated(procesarUseCase.procesar(cmd));
    }

    @Operation(summary = "Listar pings de una unidad")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/unidad/{unidadId}")
    public ResponseEntity<ApiResponse<PageResult<PingGpsResponseDto>>> listar(
            @PathVariable UUID unidadId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarPingsUnidadQuery(unidadId, user.tenantId(), page, size)));
    }

    @Operation(summary = "Histórico de pings (filtros opcionales)")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ANALISTA','OPERADOR')")
    @GetMapping("/historico")
    public ResponseEntity<ApiResponse<PageResult<PingGpsResponseDto>>> historico(
            @RequestParam(required = false) UUID unidadId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarHistoricoUseCase.listar(
                new HistoricoPingsQuery(user.tenantId(), unidadId, desde, hasta, page, size)));
    }
}
