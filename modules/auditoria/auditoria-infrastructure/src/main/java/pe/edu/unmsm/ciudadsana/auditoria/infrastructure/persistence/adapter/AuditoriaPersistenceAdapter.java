package pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.EventoAuditoriaDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.dto.OutboxEventDto;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.AuditoriaPersistencePort;
import pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.entity.EventoAuditoriaJpaEntity;
import pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.entity.OutboxEventJpaEntity;
import pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.repository.EventoAuditoriaJpaRepository;
import pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.repository.OutboxEventJpaRepository;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Repository
public class AuditoriaPersistenceAdapter implements AuditoriaPersistencePort {

    private final EventoAuditoriaJpaRepository eventoRepo;
    private final OutboxEventJpaRepository outboxRepo;

    public AuditoriaPersistenceAdapter(
            EventoAuditoriaJpaRepository eventoRepo,
            OutboxEventJpaRepository outboxRepo
    ) {
        this.eventoRepo = eventoRepo;
        this.outboxRepo = outboxRepo;
    }

    @Override
    public void saveEventoAuditoria(EventoAuditoriaDto dto) {
        EventoAuditoriaJpaEntity entity = new EventoAuditoriaJpaEntity();
        entity.setId(dto.eventoId());
        entity.setTenantId(dto.tenantId());
        entity.setUsuarioId(dto.usuarioId());
        entity.setModulo(dto.modulo());
        entity.setAccion(dto.accion());
        entity.setEntidad(dto.entidad());
        entity.setEntidadId(dto.entidadId());
        entity.setDatosAntes(dto.datosAntes());
        entity.setDatosDespues(dto.datosDespues());
        entity.setCreadoEn(dto.creadoEn() != null ? dto.creadoEn() : Instant.now());
        eventoRepo.save(entity);
    }

    @Override
    public PageResult<EventoAuditoriaDto> findEventos(
            UUID tenantId,
            String modulo,
            String entidad,
            UUID usuarioId,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            int page,
            int size
    ) {
        Instant desde = fechaDesde != null ? fechaDesde.atStartOfDay(ZoneOffset.UTC).toInstant() : null;
        Instant hasta = fechaHasta != null ? fechaHasta.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant() : null;

        Page<EventoAuditoriaJpaEntity> jpaPage = eventoRepo.findAllFiltered(
                tenantId, modulo, entidad, usuarioId, desde, hasta,
                PageRequest.of(page, size)
        );

        List<EventoAuditoriaDto> content = jpaPage.getContent().stream()
                .map(this::toDto)
                .toList();

        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }

    @Override
    public void saveOutboxEvent(OutboxEventDto dto) {
        OutboxEventJpaEntity entity = new OutboxEventJpaEntity();
        entity.setId(dto.outboxId());
        entity.setTenantId(dto.tenantId());
        entity.setAggregateType(dto.aggregateType());
        entity.setAggregateId(dto.aggregateId());
        entity.setEventType(dto.eventType());
        entity.setPayload(dto.payload());
        entity.setEstado(dto.estado());
        entity.setCreadoEn(dto.creadoEn() != null ? dto.creadoEn() : Instant.now());
        entity.setPublicadoEn(dto.publicadoEn());
        entity.setErrorMensaje(dto.errorMensaje());
        outboxRepo.save(entity);
    }

    @Override
    public PageResult<OutboxEventDto> findOutboxEvents(
            UUID tenantId,
            String estado,
            String eventType,
            int page,
            int size
    ) {
        Page<OutboxEventJpaEntity> jpaPage = outboxRepo.findAllFiltered(
                tenantId, estado, eventType,
                PageRequest.of(page, size)
        );

        List<OutboxEventDto> content = jpaPage.getContent().stream()
                .map(this::toDto)
                .toList();

        return PageResult.of(content, page, size, jpaPage.getTotalElements());
    }

    private EventoAuditoriaDto toDto(EventoAuditoriaJpaEntity e) {
        return new EventoAuditoriaDto(
                e.getId(),
                e.getTenantId(),
                e.getUsuarioId(),
                e.getModulo(),
                e.getAccion(),
                e.getEntidad(),
                e.getEntidadId(),
                e.getDatosAntes(),
                e.getDatosDespues(),
                e.getCreadoEn()
        );
    }

    private OutboxEventDto toDto(OutboxEventJpaEntity e) {
        return new OutboxEventDto(
                e.getId(),
                e.getTenantId(),
                e.getAggregateType(),
                e.getAggregateId(),
                e.getEventType(),
                e.getPayload(),
                e.getEstado(),
                e.getCreadoEn(),
                e.getPublicadoEn(),
                e.getErrorMensaje()
        );
    }
}
