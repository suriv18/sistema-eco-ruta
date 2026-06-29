package pe.edu.unmsm.ciudadsana.ciudadano.application.queryhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaFotoDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaHistorialDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.ValidacionAlertaDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ListarAlertasPorZonaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.query.ListarAlertasPorZonaQuery;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.util.List;

@Component
public class ListarAlertasPorZonaQueryHandler implements ListarAlertasPorZonaUseCase {

    private final AlertasPersistencePort alertasPersistencePort;

    public ListarAlertasPorZonaQueryHandler(AlertasPersistencePort alertasPersistencePort) {
        this.alertasPersistencePort = alertasPersistencePort;
    }

    @Override
    public Result<PageResult<AlertaResponseDto>> listarPorZona(ListarAlertasPorZonaQuery query) {
        ZonaExternoId zonaId = ZonaExternoId.of(query.zonaExternoId());
        TenantId tenantId = TenantId.of(query.tenantId());
        PageResult<AlertaCiudadana> pageResult = alertasPersistencePort.findAllByZonaAndTenantId(zonaId, tenantId, query.page(), query.size());
        return Result.success(pageResult.map(this::toDto));
    }

    private AlertaResponseDto toDto(AlertaCiudadana a) {
        List<AlertaFotoDto> fotos = a.getFotos().stream()
            .map(f -> new AlertaFotoDto(f.id().value(), f.urlArchivo(), f.tipoMime(), f.tamanioBytes()))
            .toList();
        List<AlertaHistorialDto> historial = a.getHistorial().stream()
            .map(h -> new AlertaHistorialDto(h.historialId(), h.estadoAnterior(), h.estadoNuevo(),
                h.comentario(), h.cambiadoPorUsuarioId(), h.cambiadoEn()))
            .toList();
        ValidacionAlertaDto validacionDto = a.getValidacion().map(v ->
            new ValidacionAlertaDto(v.id().value(), v.esDuplicada(),
                v.alertaOriginalId() != null ? v.alertaOriginalId().value() : null,
                v.dentroGeocerca(), v.scoreSpam(), v.resultado(), v.validadaEn())
        ).orElse(null);
        return new AlertaResponseDto(
            a.getId().value(), a.getTenantId().value(),
            a.getCiudadanoId().map(CiudadanoId::value).orElse(null),
            a.getDistritoExternoId().value(),
            a.getZonaExternoId().map(ZonaExternoId::value).orElse(null),
            a.getTitulo(), a.getDescripcion().orElse(null),
            a.getUbicacion().latitud(), a.getUbicacion().longitud(),
            a.getVolumenEstimado().name(), a.getNivelCriticidad().name(), a.getFuente().name(),
            a.getEstado().name(), fotos, historial, validacionDto,
            a.getRegistradaEn(), a.getActualizadaEn().orElse(null)
        );
    }
}
