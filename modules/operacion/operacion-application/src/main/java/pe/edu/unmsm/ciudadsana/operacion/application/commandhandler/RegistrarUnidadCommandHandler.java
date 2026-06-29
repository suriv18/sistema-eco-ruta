package pe.edu.unmsm.ciudadsana.operacion.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.command.RegistrarUnidadCommand;
import pe.edu.unmsm.ciudadsana.operacion.application.dto.UnidadResponseDto;
import pe.edu.unmsm.ciudadsana.operacion.application.port.in.RegistrarUnidadUseCase;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.UnidadesPersistencePort;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.TipoUnidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.model.Unidad;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadKg;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.CapacidadM3;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.Placa;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.UnidadId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.ErrorCode;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.UUID;

@Component
public class RegistrarUnidadCommandHandler implements RegistrarUnidadUseCase {

    private final UnidadesPersistencePort unidadesPersistencePort;
    private final OperacionEventPublisherPort eventPublisher;

    public RegistrarUnidadCommandHandler(UnidadesPersistencePort unidadesPersistencePort, OperacionEventPublisherPort eventPublisher) {
        this.unidadesPersistencePort = unidadesPersistencePort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<UnidadResponseDto> registrar(RegistrarUnidadCommand command) {
        TenantId tenantId = TenantId.of(command.tenantId());
        Placa placa;
        try {
            placa = Placa.of(command.placa());
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, e.getMessage());
        }
        if (unidadesPersistencePort.existsByPlaca(placa, tenantId)) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "Ya existe una unidad con la placa: " + command.placa());
        }
        TipoUnidad tipoUnidad;
        try {
            tipoUnidad = TipoUnidad.valueOf(command.tipoUnidad());
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorCode.VALIDACION_ERROR, "TipoUnidad inválido: " + command.tipoUnidad());
        }
        Unidad unidad = Unidad.create(
            UnidadId.of(UUID.randomUUID()),
            tenantId,
            placa,
            command.codigoInterno(),
            tipoUnidad,
            CapacidadM3.of(command.capacidadM3()),
            CapacidadKg.of(command.capacidadKg()),
            Instant.now()
        );
        unidad = unidadesPersistencePort.save(unidad);
        eventPublisher.publishAll(unidad.pullDomainEvents());
        return Result.success(new UnidadResponseDto(
            unidad.getId().value(),
            unidad.getTenantId().value(),
            unidad.getPlaca().value(),
            unidad.getCodigoInterno(),
            unidad.getTipo().name(),
            unidad.getCapacidadM3().value(),
            unidad.getCapacidadKg().value(),
            unidad.getEstadoOperativo().name(),
            unidad.getCreadoEn()
        ));
    }
}
