package pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.AgregarFotoAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.CambiarEstadoAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.RegistrarAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.ValidarAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.AgregarFotoAlertaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.CambiarEstadoAlertaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ListarAlertasPorZonaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ListarAlertasUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ObtenerAlertaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.RegistrarAlertaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ValidarAlertaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarAlertasPorZonaQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarAlertasQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ObtenerAlertaQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request.AgregarFotoAlertaRequest;
import pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request.CambiarEstadoAlertaRequest;
import pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request.RegistrarAlertaRequest;
import pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request.ValidarAlertaRequest;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.util.UUID;

@Tag(name = "Alertas Ciudadanas", description = "Gestión de alertas reportadas por ciudadanos")
@RestController
@RequestMapping("/api/v1/ciudadanos/alertas")
public class AlertaCiudadanaController {

    private final RegistrarAlertaUseCase registrarUseCase;
    private final AgregarFotoAlertaUseCase agregarFotoUseCase;
    private final CambiarEstadoAlertaUseCase cambiarEstadoUseCase;
    private final ValidarAlertaUseCase validarUseCase;
    private final ObtenerAlertaUseCase obtenerUseCase;
    private final ListarAlertasUseCase listarUseCase;
    private final ListarAlertasPorZonaUseCase listarPorZonaUseCase;
    private final CurrentUserProvider currentUser;

    public AlertaCiudadanaController(RegistrarAlertaUseCase registrarUseCase,
                                      AgregarFotoAlertaUseCase agregarFotoUseCase,
                                      CambiarEstadoAlertaUseCase cambiarEstadoUseCase,
                                      ValidarAlertaUseCase validarUseCase,
                                      ObtenerAlertaUseCase obtenerUseCase,
                                      ListarAlertasUseCase listarUseCase,
                                      ListarAlertasPorZonaUseCase listarPorZonaUseCase,
                                      CurrentUserProvider currentUser) {
        this.registrarUseCase = registrarUseCase;
        this.agregarFotoUseCase = agregarFotoUseCase;
        this.cambiarEstadoUseCase = cambiarEstadoUseCase;
        this.validarUseCase = validarUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.listarUseCase = listarUseCase;
        this.listarPorZonaUseCase = listarPorZonaUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Registrar alerta ciudadana (endpoint público)")
    @PostMapping
    public ResponseEntity<ApiResponse<AlertaResponseDto>> registrar(
            @RequestHeader("X-Tenant-Id") UUID tenantId,
            @Valid @RequestBody RegistrarAlertaRequest req) {
        var cmd = new RegistrarAlertaCommand(tenantId, req.ciudadanoId(), req.distritoExternoId(), req.zonaExternoId(),
                req.titulo(), req.descripcion(), req.latitud(), req.longitud(),
                req.volumenEstimado(), req.nivelCriticidad(), req.fuente());
        return ResultResponseMapper.toCreated(registrarUseCase.registrar(cmd));
    }

    @Operation(summary = "Agregar foto a alerta")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PostMapping("/{id}/fotos")
    public ResponseEntity<ApiResponse<AlertaResponseDto>> agregarFoto(
            @PathVariable UUID id,
            @Valid @RequestBody AgregarFotoAlertaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new AgregarFotoAlertaCommand(id, user.tenantId(), req.urlArchivo(), req.tipoMime(), req.tamanioBytes());
        return ResultResponseMapper.toCreated(agregarFotoUseCase.agregarFoto(cmd));
    }

    @Operation(summary = "Cambiar estado de alerta")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<AlertaResponseDto>> cambiarEstado(
            @PathVariable UUID id,
            @Valid @RequestBody CambiarEstadoAlertaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new CambiarEstadoAlertaCommand(id, user.tenantId(), req.nuevoEstado(), req.comentario(), req.cambiadoPorUsuarioId());
        return ResultResponseMapper.toOk(cambiarEstadoUseCase.cambiarEstado(cmd));
    }

    @Operation(summary = "Validar alerta ciudadana")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping("/{id}/validar")
    public ResponseEntity<ApiResponse<AlertaResponseDto>> validar(
            @PathVariable UUID id,
            @Valid @RequestBody ValidarAlertaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new ValidarAlertaCommand(id, user.tenantId(), req.esDuplicada(), req.alertaOriginalId(),
                req.dentroGeocerca(), req.scoreSpam(), req.resultado());
        return ResultResponseMapper.toCreated(validarUseCase.validar(cmd));
    }

    @Operation(summary = "Obtener alerta")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlertaResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerAlertaQuery(id, user.tenantId())));
    }

    @Operation(summary = "Listar alertas")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<AlertaResponseDto>>> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(new ListarAlertasQuery(user.tenantId(), estado, page, size)));
    }

    @Operation(summary = "Listar alertas por zona")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/zona/{zonaId}")
    public ResponseEntity<ApiResponse<PageResult<AlertaResponseDto>>> listarPorZona(
            @PathVariable UUID zonaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarPorZonaUseCase.listarPorZona(new ListarAlertasPorZonaQuery(zonaId, user.tenantId(), page, size)));
    }
}
