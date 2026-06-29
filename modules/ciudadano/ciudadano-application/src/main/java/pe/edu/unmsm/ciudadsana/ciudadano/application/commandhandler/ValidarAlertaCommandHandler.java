package pe.edu.unmsm.ciudadsana.ciudadano.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.ValidarAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaFotoDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaHistorialDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.ValidacionAlertaDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.ValidarAlertaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanoEventPublisherPort;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ValidacionAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ValidacionId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Component
public class ValidarAlertaCommandHandler implements ValidarAlertaUseCase {

    private final AlertasPersistencePort alertasPersistencePort;
    private final CiudadanoEventPublisherPort eventPublisher;

    public ValidarAlertaCommandHandler(AlertasPersistencePort alertasPersistencePort,
                                        CiudadanoEventPublisherPort eventPublisher) {
        this.alertasPersistencePort = alertasPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<AlertaResponseDto> validar(ValidarAlertaCommand cmd) {
        AlertaId alertaId = AlertaId.of(cmd.alertaId());
        TenantId tenantId = TenantId.of(cmd.tenantId());
        AlertaCiudadana alerta = alertasPersistencePort.findByIdAndTenantId(alertaId, tenantId)
            .orElse(null);
        if (alerta == null) {
            return Result.failure(ErrorCode.ALERTA_NO_ENCONTRADA);
        }
        ValidacionId validacionId = ValidacionId.of(UUID.randomUUID());
        AlertaId alertaOriginalId = cmd.alertaOriginalId() != null ? AlertaId.of(cmd.alertaOriginalId()) : null;
        ValidacionAlerta v = new ValidacionAlerta(validacionId, alerta.getId(), cmd.esDuplicada(),
            alertaOriginalId, cmd.dentroGeocerca(), cmd.scoreSpam(), cmd.resultado(), Instant.now());
        alerta.registrarValidacion(v, Instant.now());
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
        ValidacionAlertaDto validacionDto = a.getValidacion().map(vd ->
            new ValidacionAlertaDto(vd.id().value(), vd.esDuplicada(),
                vd.alertaOriginalId() != null ? vd.alertaOriginalId().value() : null,
                vd.dentroGeocerca(), vd.scoreSpam(), vd.resultado(), vd.validadaEn())
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
