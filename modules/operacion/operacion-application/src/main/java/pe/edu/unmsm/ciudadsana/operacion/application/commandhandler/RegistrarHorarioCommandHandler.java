package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarHorarioCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.HorarioResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarHorarioUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.HorariosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.HorarioRecoleccion;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarHorarioCommandHandler implements RegistrarHorarioUseCase {

    private final HorariosPersistencePort horariosPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public RegistrarHorarioCommandHandler(HorariosPersistencePort horariosPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.horariosPersistencePort = horariosPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<HorarioResponseDto> registrar(RegistrarHorarioCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        ZonaId zonaId = ZonaId.of(command.zonaId());
        if (horariosPersistencePort.existsByZonaAndDiaAndHorario(zonaId, command.diaSemana(), command.horaInicio(), command.horaFin(), tenantId)) {
            return Result.failure(ErrorCode.HORARIO_DUPLICADO, "Ya existe un horario con los mismos datos para esta zona");
        }
        HorarioRecoleccion horario;
        try {
            horario = HorarioRecoleccion.create(
                HorarioRecoleccionId.of(UUID.randomUUID()),
                tenantId,
                zonaId,
                command.diaSemana(),
                command.horaInicio(),
                command.horaFin(),
                command.observacion(),
                Instant.now()
            );
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorCode.HORARIO_RANGO_INVALIDO, e.getMessage());
        }
        horario = horariosPersistencePort.save(horario);
        eventPublisher.publishAll(horario.pullDomainEvents());
        return Result.success(toDto(horario));
    }

    private HorarioResponseDto toDto(HorarioRecoleccion h) {
        return new HorarioResponseDto(
            h.getId().value(),
            h.getTenantId().value(),
            h.getZonaId().value(),
            h.getDiaSemana(),
            h.getHoraInicio(),
            h.getHoraFin(),
            h.getObservacion(),
            h.getEstado().name(),
            h.getCreadoEn()
        );
    }
}
