package pe.edu.unmsm.ciudadsana.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.entity.OutboxEventJpaEntity;
import pe.edu.unmsm.ciudadsana.auditoria.infrastructure.persistence.repository.OutboxEventJpaRepository;
import pe.edu.unmsm.ciudadsana.rutas.application.command.ReoptimizarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.ReoptimizarRutaUseCase;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxDispatcher {

    private static final Logger log = LoggerFactory.getLogger(OutboxDispatcher.class);
    private static final String PENDIENTE = "PENDIENTE";
    private static final String ALERTA_CRITICA = "AlertaCriticaDetectadaEvent";

    private final OutboxEventJpaRepository outboxRepo;
    private final ReoptimizarRutaUseCase reoptimizarUseCase;

    public OutboxDispatcher(OutboxEventJpaRepository outboxRepo,
                             ReoptimizarRutaUseCase reoptimizarUseCase) {
        this.outboxRepo = outboxRepo;
        this.reoptimizarUseCase = reoptimizarUseCase;
    }

    @Scheduled(fixedDelayString = "${outbox.dispatcher.interval-ms:5000}")
    @Transactional
    public void dispatch() {
        List<OutboxEventJpaEntity> pendientes = outboxRepo.findByEstadoOrderByCreadoEnAsc(PENDIENTE);
        for (OutboxEventJpaEntity evento : pendientes) {
            try {
                if (ALERTA_CRITICA.equals(evento.getEventType())) {
                    procesarAlertaCritica(evento);
                } else {
                    // Tipo no reconocido — marcar como publicado (no hay handler para él)
                    marcarPublicado(evento);
                }
            } catch (Exception e) {
                log.error("Error procesando OutboxEvent {}: {}", evento.getId(), e.getMessage(), e);
                marcarError(evento, e.getMessage());
            }
        }
    }

    private void procesarAlertaCritica(OutboxEventJpaEntity evento) {
        // aggregateId es el rutaId, tenantId es el tenantId
        // Llamada mínima sin unidades/zonas — ReoptimizarRutaCommandHandler
        // busca la ruta por id y usa los datos de la ruta existente.
        // Para MVP: listas vacías de unidades/zonas — el handler las recibe del request
        // en este caso no hay request body, así que se pasa listas vacías y el handler
        // intentará reoptimizar (si el optimizador no recibe unidades, puede fallar —
        // se registra el error en el outbox).
        ReoptimizarRutaCommand cmd = new ReoptimizarRutaCommand(
                evento.getTenantId(),
                evento.getAggregateId(),
                List.of(),   // unidades — vacío en dispatch automático (MVP)
                List.of(),   // zonas — vacío en dispatch automático (MVP)
                List.of(),   // alertasCriticas
                null,        // parametrosSolver — usa defaults del handler
                "ALERTA_CRITICA"
        );
        Result<?> resultado = reoptimizarUseCase.reoptimizar(cmd);
        if (resultado.isSuccess()) {
            marcarPublicado(evento);
            log.info("OutboxEvent {} procesado: ruta {} reoptimizada", evento.getId(), evento.getAggregateId());
        } else {
            String msg = resultado.getError().message();
            marcarError(evento, msg);
            log.warn("OutboxEvent {} falló reoptimización: {}", evento.getId(), msg);
        }
    }

    private void marcarPublicado(OutboxEventJpaEntity evento) {
        evento.setEstado("PUBLICADO");
        evento.setPublicadoEn(Instant.now());
        outboxRepo.save(evento);
    }

    private void marcarError(OutboxEventJpaEntity evento, String mensaje) {
        evento.setEstado("ERROR");
        evento.setErrorMensaje(mensaje != null ? mensaje.substring(0, Math.min(mensaje.length(), 500)) : "Error desconocido");
        outboxRepo.save(evento);
    }
}
