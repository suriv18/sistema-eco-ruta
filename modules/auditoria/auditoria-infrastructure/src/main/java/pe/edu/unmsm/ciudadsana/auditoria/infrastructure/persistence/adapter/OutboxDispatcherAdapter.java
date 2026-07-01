package pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.adapter;

import org.springframework.stereotype.Repository;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.OutboxDispatcherPort;
import pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.repository.OutboxEventJpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public class OutboxDispatcherAdapter implements OutboxDispatcherPort {

    private final OutboxEventJpaRepository outboxRepo;

    public OutboxDispatcherAdapter(OutboxEventJpaRepository outboxRepo) {
        this.outboxRepo = outboxRepo;
    }

    @Override
    public List<OutboxEventView> findPendientes() {
        return outboxRepo.findByEstadoOrderByCreadoEnAsc("PENDIENTE").stream()
                .map(e -> new OutboxEventView(e.getId(), e.getTenantId(), e.getAggregateId(), e.getEventType(), e.getPayload()))
                .toList();
    }

    @Override
    public void marcarPublicado(UUID id, Instant publicadoEn) {
        outboxRepo.findById(id).ifPresent(e -> {
            e.setEstado("PUBLICADO");
            e.setPublicadoEn(publicadoEn);
            outboxRepo.save(e);
        });
    }

    @Override
    public void marcarError(UUID id, String errorMensaje) {
        outboxRepo.findById(id).ifPresent(e -> {
            e.setEstado("ERROR");
            e.setErrorMensaje(errorMensaje != null ? errorMensaje.substring(0, Math.min(errorMensaje.length(), 500)) : "Error desconocido");
            outboxRepo.save(e);
        });
    }
}
