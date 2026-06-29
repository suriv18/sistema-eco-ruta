package pe.edu.unmsm.ciudadsana.kpi.interfaces.rest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.unmsm.ciudadsana.kpi.application.command.CalcularResumenDiarioCommand;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiAlertaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiRutaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiUnidadDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiZonaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.ResumenOperativoDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.CalcularResumenDiarioUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ListarKpisAlertaUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ListarKpisRutaUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ListarKpisUnidadUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ListarKpisZonaUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.ObtenerResumenDiarioUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisAlertaQuery;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisRutaQuery;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisUnidadQuery;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ListarKpisZonaQuery;
import pe.edu.unmsm.ciudadsana.kpi.application.query.ObtenerResumenDiarioQuery;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;
import pe.edu.unmsm.ciudadsana.shared.security.context.CurrentUserProvider;
import pe.edu.unmsm.ciudadsana.shared.web.util.ResultResponseMapper;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/kpis")
public class KpiController {

    private final ObtenerResumenDiarioUseCase obtenerResumenUseCase;
    private final CalcularResumenDiarioUseCase calcularResumenUseCase;
    private final ListarKpisRutaUseCase listarRutasUseCase;
    private final ListarKpisUnidadUseCase listarUnidadesUseCase;
    private final ListarKpisZonaUseCase listarZonasUseCase;
    private final ListarKpisAlertaUseCase listarAlertasUseCase;
    private final CurrentUserProvider currentUserProvider;

    public KpiController(
            ObtenerResumenDiarioUseCase obtenerResumenUseCase,
            CalcularResumenDiarioUseCase calcularResumenUseCase,
            ListarKpisRutaUseCase listarRutasUseCase,
            ListarKpisUnidadUseCase listarUnidadesUseCase,
            ListarKpisZonaUseCase listarZonasUseCase,
            ListarKpisAlertaUseCase listarAlertasUseCase,
            CurrentUserProvider currentUserProvider) {
        this.obtenerResumenUseCase = obtenerResumenUseCase;
        this.calcularResumenUseCase = calcularResumenUseCase;
        this.listarRutasUseCase = listarRutasUseCase;
        this.listarUnidadesUseCase = listarUnidadesUseCase;
        this.listarZonasUseCase = listarZonasUseCase;
        this.listarAlertasUseCase = listarAlertasUseCase;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/resumen-diario")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'ANALISTA')")
    public ResponseEntity<?> obtenerResumenDiario(
            @RequestParam UUID distritoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        UUID tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Result<ResumenOperativoDto> result = obtenerResumenUseCase.obtener(
                new ObtenerResumenDiarioQuery(tenantId, distritoId, fecha));
        return ResultResponseMapper.toOk(result);
    }

    @PostMapping("/resumen-diario/calcular")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<?> calcularResumenDiario(
            @RequestParam UUID distritoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        UUID tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Result<ResumenOperativoDto> result = calcularResumenUseCase.calcular(
                new CalcularResumenDiarioCommand(tenantId, distritoId, fecha));
        return ResultResponseMapper.toOk(result);
    }

    @GetMapping("/rutas")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'ANALISTA')")
    public ResponseEntity<?> listarKpisRuta(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Result<PageResult<KpiRutaDto>> result = listarRutasUseCase.listar(
                new ListarKpisRutaQuery(tenantId, fechaDesde, fechaHasta, page, size));
        return ResultResponseMapper.toOk(result);
    }

    @GetMapping("/unidades")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'ANALISTA')")
    public ResponseEntity<?> listarKpisUnidad(
            @RequestParam(required = false) UUID unidadId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Result<PageResult<KpiUnidadDto>> result = listarUnidadesUseCase.listar(
                new ListarKpisUnidadQuery(tenantId, unidadId, fechaDesde, fechaHasta, page, size));
        return ResultResponseMapper.toOk(result);
    }

    @GetMapping("/zonas")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'ANALISTA')")
    public ResponseEntity<?> listarKpisZona(
            @RequestParam(required = false) UUID zonaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Result<PageResult<KpiZonaDto>> result = listarZonasUseCase.listar(
                new ListarKpisZonaQuery(tenantId, zonaId, fechaDesde, fechaHasta, page, size));
        return ResultResponseMapper.toOk(result);
    }

    @GetMapping("/alertas")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'ANALISTA')")
    public ResponseEntity<?> listarKpisAlerta(
            @RequestParam(required = false) UUID zonaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Result<PageResult<KpiAlertaDto>> result = listarAlertasUseCase.listar(
                new ListarKpisAlertaQuery(tenantId, zonaId, fechaDesde, fechaHasta, page, size));
        return ResultResponseMapper.toOk(result);
    }
}
