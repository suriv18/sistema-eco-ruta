package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public Result<HorarioResponseDto> registrar(RegistrarHorarioCommand cmd) {
        TenantId tenantId = TenantId.of(cmd.tenantId());
        ZonaId zonaId = ZonaId.of(cmd.zonaId());
        if (horariosPersistencePort.existsByZonaAndDiaAndHorario(zonaId, cmd.diaSemana(), cmd.horaInicio(), cmd.horaFin(), tenantId)) {
            return Result.failure(ErrorCode.HORARIO_DUPLICADO, "Ya existe un horario con los mismos datos para esta zona");
        }
        if (cmd.horaFin() != null && cmd.horaInicio() != null && !cmd.horaFin().isAfter(cmd.horaInicio())) {
            return Result.failure(ErrorCode.HORARIO_RANGO_INVALIDO);
        }
        HorarioRecoleccion horario = HorarioRecoleccion.create(
            HorarioRecoleccionId.of(UUID.randomUUID()),
            tenantId,
            zonaId,
            cmd.diaSemana(),
            cmd.horaInicio(),
            cmd.horaFin(),
            cmd.observacion(),
            Instant.now()
        );
        var eventos = horario.pullDomainEvents();
        HorarioRecoleccion guardado = horariosPersistencePort.save(horario);
        eventPublisher.publishAll(eventos);
        return Result.success(HorarioResponseDto.from(guardado));
    }
}
