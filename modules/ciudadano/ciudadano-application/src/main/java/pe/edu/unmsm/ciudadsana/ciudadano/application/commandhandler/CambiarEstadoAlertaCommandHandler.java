package pe.edu.unmsm.ciudadsana.ciudadano.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.command.CambiarEstadoAlertaCommand;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaFotoDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaHistorialDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.AlertaResponseDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.dto.ValidacionAlertaDto;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.in.CambiarEstadoAlertaUseCase;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.AlertasPersistencePort;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanoEventPublisherPort;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.enums.EstadoAlerta;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.model.AlertaCiudadana;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.AlertaId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.CiudadanoId;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.valueobject.ZonaExternoId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;

@Component
public class CambiarEstadoAlertaCommandHandler implements CambiarEstadoAlertaUseCase {

    private final AlertasPersistencePort alertasPersistencePort;
    private final CiudadanoEventPublisherPort eventPublisher;

    public CambiarEstadoAlertaCommandHandler(AlertasPersistencePort alertasPersistencePort,
                                              CiudadanoEventPublisherPort eventPublisher) {
        this.alertasPersistencePort = alertasPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<AlertaResponseDto> cambiarEstado(CambiarEstadoAlertaCommand cmd) {
        AlertaId alertaId = AlertaId.of(cmd.alertaId());
        TenantId tenantId = TenantId.of(cmd.tenantId());
        AlertaCiudadana alerta = alertasPersistencePort.findByIdAndTenantId(alertaId, tenantId)
            .orElse(null);
        if (alerta == null) {
            return Result.failure(ErrorCode.ALERTA_NO_ENCONTRADA);
        }
        EstadoAlerta nuevoEstado = EstadoAlerta.valueOf(cmd.nuevoEstado());
        try {
            alerta.cambiarEstado(nuevoEstado, cmd.comentario(), cmd.cambiadoPorUsuarioId(), Instant.now());
        } catch (IllegalStateException e) {
            return Result.failure(ErrorCode.TRANSICION_ESTADO_INVALIDA, e.getMessage());
        }
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
