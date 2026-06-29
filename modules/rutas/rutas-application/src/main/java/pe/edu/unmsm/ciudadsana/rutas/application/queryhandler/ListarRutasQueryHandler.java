package pe.edu.unmsm.ciudadsana.rutas.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.MetricasRutaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaParadaDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaResponseDto;
import pe.edu.unmsm.ciudadsana.rutas.application.dto.RutaVersionDto;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.ListarRutasUseCase;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasPersistencePort;
import pe.edu.unmsm.ciudadsana.rutas.application.query.ListarRutasQuery;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.Ruta;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaParada;
import pe.edu.unmsm.ciudadsana.rutas.domain.model.RutaVersion;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.AlertaExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.ContenedorExternoId;
import pe.edu.unmsm.ciudadsana.rutas.domain.valueobject.MetricasRuta;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

@Component
public class ListarRutasQueryHandler implements ListarRutasUseCase {

    private final RutasPersistencePort rutasPersistencePort;

    public ListarRutasQueryHandler(RutasPersistencePort rutasPersistencePort) {
        this.rutasPersistencePort = rutasPersistencePort;
    }

    @Override
    public Result<PageResult<RutaResponseDto>> listar(ListarRutasQuery query) {
        TenantId tenantId = TenantId.of(query.tenantId());
        PageResult<Ruta> pageResult = rutasPersistencePort.findAllByTenantId(
                tenantId, query.distritoId(), query.fecha(), query.estado(),
                query.page(), query.size()
        );
        return Result.success(pageResult.map(this::toDto));
    }

    private MetricasRutaDto toMetricasDto(MetricasRuta m) {
        return new MetricasRutaDto(m.distanciaM(), m.duracionS(), m.cargaKg());
    }

    private RutaParadaDto toParadaDto(RutaParada p) {
        return new RutaParadaDto(
                p.getId().value(), p.getRutaVersionId().value(), p.getZonaId().value(),
                p.getContenedorId().map(ContenedorExternoId::value).orElse(null),
                p.getOrden(), p.getEta().orElse(null),
                p.getHoraLlegadaReal().orElse(null), p.getHoraSalidaReal().orElse(null),
                p.getDemandaEstimadaKg(), p.getCargaAcumuladaKg(),
                p.getEstado().name(), p.getCreadoEn()
        );
    }

    private RutaVersionDto toVersionDto(RutaVersion v) {
        return new RutaVersionDto(
                v.getId().value(), v.getRutaId().value(), v.getVersion(),
                v.getMotivo().name(),
                v.getAlertaIdExterno().map(AlertaExternoId::value).orElse(null),
                v.getGeneradoPor().name(), toMetricasDto(v.getMetricas()),
                v.getParadas().stream().map(this::toParadaDto).toList(),
                v.getCreadoEn()
        );
    }

    private RutaResponseDto toDto(Ruta r) {
        return new RutaResponseDto(
                r.getId().value(), r.getTenantId().value(),
                r.getTurnoId().value(), r.getDistritoId().value(),
                r.getDepositoOrigenId().value(), r.getDepositoDestinoId().value(),
                r.getFecha(), r.getTipoRuta().name(), r.getEstado().name(),
                toMetricasDto(r.getMetricas()),
                r.getVersionActual().map(this::toVersionDto).orElse(null),
                r.getCreadoEn(), r.getActualizadoEn().orElse(null)
        );
    }
}
