package pe.edu.unmsm.ciudadsana.rutas.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.unmsm.ciudadsana.rutas.application.command.*;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaDetalleResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.*;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ListarRutasQuery;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ObtenerRutaDetalleQuery;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ObtenerRutaQuery;
import pe.edu.unmsm.ciudadsana.rutas.interfaces.rest.request.*;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.response.ApiResponse;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.time.LocalDate;
import java.util.UUID;

@Tag(name = "Rutas", description = "Gestión de rutas de recolección")
@RestController
@RequestMapping("/api/v1/rutas")
public class RutaController {

    private final CrearRutaUseCase crearUseCase;
    private final ObtenerRutaUseCase obtenerUseCase;
    private final ObtenerRutaDetalleUseCase obtenerDetalleUseCase;
    private final ListarRutasUseCase listarUseCase;
    private final AprobarRutaUseCase aprobarUseCase;
    private final IniciarEjecucionRutaUseCase iniciarEjecucionUseCase;
    private final FinalizarRutaUseCase finalizarUseCase;
    private final CancelarRutaUseCase cancelarUseCase;
    private final AgregarVersionRutaUseCase agregarVersionUseCase;
    private final ActualizarParadaUseCase actualizarParadaUseCase;
    private final RegistrarEventoRutaUseCase registrarEventoUseCase;
    private final OptimizarRutaUseCase optimizarUseCase;
    private final ReoptimizarRutaUseCase reoptimizarUseCase;
    private final CurrentUserProvider currentUser;

    public RutaController(CrearRutaUseCase crearUseCase,
                          ObtenerRutaUseCase obtenerUseCase,
                          ObtenerRutaDetalleUseCase obtenerDetalleUseCase,
                          ListarRutasUseCase listarUseCase,
                          AprobarRutaUseCase aprobarUseCase,
                          IniciarEjecucionRutaUseCase iniciarEjecucionUseCase,
                          FinalizarRutaUseCase finalizarUseCase,
                          CancelarRutaUseCase cancelarUseCase,
                          AgregarVersionRutaUseCase agregarVersionUseCase,
                          ActualizarParadaUseCase actualizarParadaUseCase,
                          RegistrarEventoRutaUseCase registrarEventoUseCase,
                          OptimizarRutaUseCase optimizarUseCase,
                          ReoptimizarRutaUseCase reoptimizarUseCase,
                          CurrentUserProvider currentUser) {
        this.crearUseCase = crearUseCase;
        this.obtenerUseCase = obtenerUseCase;
        this.obtenerDetalleUseCase = obtenerDetalleUseCase;
        this.listarUseCase = listarUseCase;
        this.aprobarUseCase = aprobarUseCase;
        this.iniciarEjecucionUseCase = iniciarEjecucionUseCase;
        this.finalizarUseCase = finalizarUseCase;
        this.cancelarUseCase = cancelarUseCase;
        this.agregarVersionUseCase = agregarVersionUseCase;
        this.actualizarParadaUseCase = actualizarParadaUseCase;
        this.registrarEventoUseCase = registrarEventoUseCase;
        this.optimizarUseCase = optimizarUseCase;
        this.reoptimizarUseCase = reoptimizarUseCase;
        this.currentUser = currentUser;
    }

    @Operation(summary = "Optimizar ruta automáticamente (llama al optimizador externo)")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping("/optimizar")
    public ResponseEntity<ApiResponse<RutaResponseDto>> optimizar(
            @Valid @RequestBody OptimizarRutaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new OptimizarRutaCommand(
                user.tenantId(), req.turnoId(), req.distritoId(),
                req.depositoOrigenId(), req.depositoDestinoId(),
                req.fecha(), req.tipoRuta(),
                req.unidades(), req.zonas(),
                req.alertasCriticas(), req.parametrosSolver()
        );
        return ResultResponseMapper.toCreated(optimizarUseCase.optimizar(cmd));
    }

    @Operation(summary = "Reoptimizar ruta (nueva versión automática)")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping("/{id}/reoptimizar")
    public ResponseEntity<ApiResponse<RutaResponseDto>> reoptimizar(
            @PathVariable UUID id,
            @Valid @RequestBody ReoptimizarRutaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new ReoptimizarRutaCommand(
                user.tenantId(), id,
                req.unidades(), req.zonas(),
                req.alertasCriticas(), req.parametrosSolver(), req.motivo()
        );
        return ResultResponseMapper.toCreated(reoptimizarUseCase.reoptimizar(cmd));
    }

    @Operation(summary = "Crear ruta de recolección")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<RutaResponseDto>> crear(@Valid @RequestBody CrearRutaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new CrearRutaCommand(user.tenantId(), req.turnoId(), req.distritoId(),
                req.depositoOrigenId(), req.depositoDestinoId(), req.fecha(), req.tipoRuta());
        return ResultResponseMapper.toCreated(crearUseCase.crear(cmd));
    }

    @Operation(summary = "Obtener ruta por ID")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RutaResponseDto>> obtener(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerUseCase.obtener(new ObtenerRutaQuery(id, user.tenantId())));
    }

    @Operation(summary = "Obtener detalle completo de ruta (con versiones, paradas y eventos)")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping("/{id}/detalle")
    public ResponseEntity<ApiResponse<RutaDetalleResponseDto>> obtenerDetalle(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(obtenerDetalleUseCase.obtenerDetalle(new ObtenerRutaDetalleQuery(id, user.tenantId())));
    }

    @Operation(summary = "Listar rutas con filtros opcionales por distrito, fecha y estado")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResult<RutaResponseDto>>> listar(
            @RequestParam(required = false) UUID distritoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(listarUseCase.listar(
                new ListarRutasQuery(user.tenantId(), distritoId, fecha, estado, page, size)));
    }

    @Operation(summary = "Aprobar ruta para su ejecución")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<ApiResponse<RutaResponseDto>> aprobar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(aprobarUseCase.aprobar(new AprobarRutaCommand(id, user.tenantId())));
    }

    @Operation(summary = "Iniciar ejecución de ruta")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PatchMapping("/{id}/iniciar-ejecucion")
    public ResponseEntity<ApiResponse<RutaResponseDto>> iniciarEjecucion(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(iniciarEjecucionUseCase.iniciarEjecucion(
                new IniciarEjecucionRutaCommand(id, user.tenantId())));
    }

    @Operation(summary = "Finalizar ruta de recolección")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<ApiResponse<RutaResponseDto>> finalizar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(finalizarUseCase.finalizar(new FinalizarRutaCommand(id, user.tenantId())));
    }

    @Operation(summary = "Cancelar ruta de recolección")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ApiResponse<RutaResponseDto>> cancelar(@PathVariable UUID id) {
        var user = currentUser.requireCurrentUser();
        return ResultResponseMapper.toOk(cancelarUseCase.cancelar(new CancelarRutaCommand(id, user.tenantId())));
    }

    @Operation(summary = "Agregar nueva versión de ruta (reoptimización)")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    @PostMapping("/{id}/versiones")
    public ResponseEntity<ApiResponse<RutaResponseDto>> agregarVersion(
            @PathVariable UUID id,
            @Valid @RequestBody AgregarVersionRutaRequest req) {
        var user = currentUser.requireCurrentUser();
        var paradas = req.paradas().stream()
                .map(p -> new AgregarVersionRutaCommand.NuevaParadaDto(
                        p.zonaId(), p.contenedorId(), p.orden(), p.eta(), p.demandaEstimadaKg()))
                .toList();
        var cmd = new AgregarVersionRutaCommand(user.tenantId(), id, req.motivo(), req.alertaIdExterno(),
                req.generadoPor(), req.distanciaM(), req.duracionS(), req.cargaKg(), paradas);
        return ResultResponseMapper.toCreated(agregarVersionUseCase.agregarVersion(cmd));
    }

    @Operation(summary = "Actualizar estado de una parada de la ruta")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PatchMapping("/{id}/paradas/{paradaId}")
    public ResponseEntity<ApiResponse<RutaResponseDto>> actualizarParada(
            @PathVariable UUID id,
            @PathVariable UUID paradaId,
            @Valid @RequestBody ActualizarParadaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new ActualizarParadaCommand(id, paradaId, user.tenantId(),
                req.nuevoEstado(), req.horaLlegada(), req.horaSalida());
        return ResultResponseMapper.toOk(actualizarParadaUseCase.actualizar(cmd));
    }

    @Operation(summary = "Registrar evento en la ruta (incidente, desvío, pausa, etc.)")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','OPERADOR')")
    @PostMapping("/{id}/eventos")
    public ResponseEntity<ApiResponse<RutaResponseDto>> registrarEvento(
            @PathVariable UUID id,
            @Valid @RequestBody RegistrarEventoRutaRequest req) {
        var user = currentUser.requireCurrentUser();
        var cmd = new RegistrarEventoRutaCommand(id, user.tenantId(),
                req.tipoEvento(), req.descripcion(), req.datosJson());
        return ResultResponseMapper.toCreated(registrarEventoUseCase.registrar(cmd));
    }
}
