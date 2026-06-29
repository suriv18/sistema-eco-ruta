package pe.edu.unmsm.ciudadsana.ciudadano.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.RegistrarAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaFotoDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaHistorialDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.ValidacionAlertaDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.RegistrarAlertaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanoEventPublisherPort;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.FuenteAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.NivelCriticidad;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.VolumenEstimado;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.Coordenadas;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.DistritoExternoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class RegistrarAlertaCommandHandler implements RegistrarAlertaUseCase {

    private final AlertasPersistencePort alertasPersistencePort;
    private final CiudadanoEventPublisherPort eventPublisher;

    public RegistrarAlertaCommandHandler(AlertasPersistencePort alertasPersistencePort,
                                          CiudadanoEventPublisherPort eventPublisher) {
        this.alertasPersistencePort = alertasPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<AlertaResponseDto> registrar(RegistrarAlertaCommand cmd) {
        AlertaId id = AlertaId.of(UUID.randomUUID());
        TenantId tenantId = TenantId.of(cmd.tenantId());
        CiudadanoId ciudadanoId = cmd.ciudadanoId() != null ? CiudadanoId.of(cmd.ciudadanoId()) : null;
        DistritoExternoId distritoId = DistritoExternoId.of(cmd.distritoExternoId());
        ZonaExternoId zonaId = cmd.zonaExternoId() != null ? ZonaExternoId.of(cmd.zonaExternoId()) : null;
        Coordenadas coords = Coordenadas.of(cmd.latitud(), cmd.longitud());
        VolumenEstimado volumen = VolumenEstimado.valueOf(cmd.volumenEstimado());
        NivelCriticidad criticidad = NivelCriticidad.valueOf(cmd.nivelCriticidad());
        FuenteAlerta fuente = FuenteAlerta.valueOf(cmd.fuente());
        AlertaCiudadana alerta = AlertaCiudadana.create(id, tenantId, ciudadanoId, distritoId, zonaId,
            cmd.titulo(), cmd.descripcion(), coords, volumen, criticidad, fuente, Instant.now());
        alertasPersistencePort.save(alerta);
        eventPublisher.publishAll(alerta.pullDomainEvents());
        return Result.success(toDto(alerta));
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
