package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.CrearTurnoCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.TurnoResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.CrearTurnoUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.ChoferesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.DistritosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.TurnosPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoTurno;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Chofer;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Turno;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ChoferId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.DistritoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.TurnoId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class CrearTurnoCommandHandler implements CrearTurnoUseCase {

    private final UnidadesPersistencePort unidadesPersistencePort;
    private final ChoferesPersistencePort choferesPersistencePort;
    private final DistritosPersistencePort distritosPersistencePort;
    private final TurnosPersistencePort turnosPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public CrearTurnoCommandHandler(UnidadesPersistencePort unidadesPersistencePort, ChoferesPersistencePort choferesPersistencePort, DistritosPersistencePort distritosPersistencePort, TurnosPersistencePort turnosPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.unidadesPersistencePort = unidadesPersistencePort;
        this.choferesPersistencePort = choferesPersistencePort;
        this.distritosPersistencePort = distritosPersistencePort;
        this.turnosPersistencePort = turnosPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<TurnoResponseDto> crear(CrearTurnoCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        UnidadId unidadId = UnidadId.of(command.unidadId());
        ChoferId choferId = ChoferId.of(command.choferId());
        DistritoId distritoId = DistritoId.of(command.distritoId());
        Optional<Unidad> unidadOpt = unidadesPersistencePort.findById(unidadId, tenantId);
        if (unidadOpt.isEmpty()) return Result.failure(ErrorCode.UNIDAD_NO_ENCONTRADA);
        Unidad unidad = unidadOpt.get();
        if (!unidad.estaDisponible()) return Result.failure(ErrorCode.UNIDAD_NO_DISPONIBLE);
        Optional<Chofer> choferOpt = choferesPersistencePort.findById(choferId, tenantId);
        if (choferOpt.isEmpty()) return Result.failure(ErrorCode.CHOFER_NO_ENCONTRADO);
        Chofer chofer = choferOpt.get();
        if (!chofer.estaDisponible()) return Result.failure(ErrorCode.CHOFER_NO_DISPONIBLE);
        TipoTurno tipoTurno;
        try {
            tipoTurno = TipoTurno.valueOf(command.tipoTurno());
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "TipoTurno inválido: " + command.tipoTurno());
        }
        if (turnosPersistencePort.existeSuperposicionUnidad(unidadId, command.fecha(), command.horaInicio(), command.horaFin(), tenantId)) {
            return Result.failure(ErrorCode.TURNO_SUPERPUESTO, "La unidad ya tiene un turno asignado en ese horario");
        }
        if (turnosPersistencePort.existeSuperposicionChofer(choferId, command.fecha(), command.horaInicio(), command.horaFin(), tenantId)) {
            return Result.failure(ErrorCode.TURNO_SUPERPUESTO, "El chofer ya tiene un turno asignado en ese horario");
        }
        Turno turno;
        try {
            turno = Turno.create(
                TurnoId.of(UUID.randomUUID()),
                tenantId,
                unidadId,
                choferId,
                distritoId,
                command.fecha(),
                command.horaInicio(),
                command.horaFin(),
                tipoTurno,
                Instant.now()
            );
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorCode.TURNO_INVALIDO, e.getMessage());
        }
        turno = turnosPersistencePort.save(turno);
        eventPublisher.publishAll(turno.pullDomainEvents());
        return Result.success(new TurnoResponseDto(
            turno.getId().value(),
            turno.getTenantId().value(),
            turno.getUnidadId().value(),
            turno.getChoferId().value(),
            turno.getDistritoId().value(),
            turno.getFecha(),
            turno.getHoraInicio(),
            turno.getHoraFin(),
            turno.getTipo().name(),
            turno.getEstado().name(),
            turno.getCreadoEn()
        ));
    }
}
