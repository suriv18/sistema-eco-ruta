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
import pe.edu.unmsm.ciudadsana.telemetria.application.command.AtenderDesvioCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.DescartarDesvioCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.command.RegistrarDesvioRutaCommand;
import pe.edu.unmsm.ciudadsana.telemetria.application.dto.DesvioRutaResponseDto;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.AtenderDesvioUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.DescartarDesvioUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.ListarDesviosActivosUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.in.RegistrarDesvioRutaUseCase;
import pe.edu.unmsm.ciudadsana.telemetria.application.query.ListarDesviosActivosQuery;
import pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest.request.RegistrarDesvioRutaRequest;

import java.util.UUID;

@Tag(name = "Desvíos de Ruta", description = "Detección y gestión de desvíos de ruta de unidades")
@RestController
@RequestMapping("/api/v1/telemetria/desvios")
public class DesvioRutaController {

    private final RegistrarDesvioRutaUseCase registrarUseCase;
    private final ListarDesviosActivosUseCase listarUseCase;
    private final AtenderDesvioUseCase atenderUseCase;
    private final DescartarDesvioUseCase descartarUseCase;
    private final CurrentUserProvider currentUser;

    public DesvioRutaController(RegistrarDesvioRutaUseCase registrarUseCase,
                                ListarDesviosActivosUseCase listarUseCase,
                                AtenderDesvioUseCase atenderUseCase,
                                DescartarDesvioUseCase descartarUseCase,
                                CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.listarUseCase = listarUseCase;
        this.atenderUseCase = atenderUseCase;
        this.descartarUseCase = descartarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar desvío de ruta")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<DesvioRutaResponseDto>> registrar(
            @Valid @RequestBody RegistrarDesvioRutaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarDesvioRutaCommand(
                user.tenantId(),
                req.unidadExternoId(),
                req.rutaExternoId(),
                req.latitud(),
                req.longitud(),
                req.distanciaDesvioM(),
                req.severidad()
        );
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Listar desvíos activos de una ruta")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/ruta/{rutaId}")
    public ResponseEntity<ApiResponse<PageResult<DesvioRutaResponseDto>>> listarActivos(
            @PathVariable UUID rutaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarDesviosActivosQuery(rutaId, user.tenantId(), page, size)));
    }

    @Operation(summary = "Atender desvío de ruta")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PatchMapping("/{id}/atender")
    public ResponseEntity<ApiResponse<Void>> atender(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(atenderUseCase.atender(new AtenderDesvioCommand(id, user.tenantId())));
    }

    @Operation(summary = "Descartar desvío de ruta")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PatchMapping("/{id}/descartar")
    public ResponseEntity<ApiResponse<Void>> descartar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toNoContent(descartarUseCase.descartar(new DescartarDesvioCommand(id, user.tenantId())));
    }
}
