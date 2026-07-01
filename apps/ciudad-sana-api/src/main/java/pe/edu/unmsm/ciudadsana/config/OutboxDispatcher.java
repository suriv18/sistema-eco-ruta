package pe.edu.unmsm.ciudadsana.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.unmsm.ciudadsana.auditoria.application.port.out.OutboxDispatcherPort;
import pe.edu.unmsm.ciudadsana.rutas.application.command.ReoptimizarRutaCommand;
import pe.edu.unmsm.ciudadsana.rutas.application.port.in.ReoptimizarRutaUseCase;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.time.Instant;
import java.util.List;

@Component
public class OutboxDispatcher {

    private static final Logger log = LoggerFactory.getLogger(OutboxDispatcher.class);
    private static final String ALERTA_CRITICA = "AlertaCriticaDetectadaEvent";

    private final OutboxDispatcherPort outboxPort;
    private final ReoptimizarRutaUseCase reoptimizarUseCase;

    public OutboxDispatcher(OutboxDispatcherPort outboxPort,
                             ReoptimizarRutaUseCase reoptimizarUseCase) {
        this.outboxPort = outboxPort;
        this.reoptimizarUseCase = reoptimizarUseCase;
    }

    @Scheduled(fixedDelayString = "${outbox.dispatcher.interval-ms:5000}")
    @Transactional
    public void dispatch() {
        List<OutboxDispatcherPort.OutboxEventView> pendientes = outboxPort.findPendientes();
        for (OutboxDispatcherPort.OutboxEventView evento : pendientes) {
            try {
                if (ALERTA_CRITICA.equals(evento.eventType())) {
                    procesarAlertaCritica(evento);
                } else {
                    // Tipo no reconocido — marcar como publicado (no hay handler para él)
                    outboxPort.marcarPublicado(evento.id(), Instant.now());
                }
            } catch (Exception e) {
                log.error("Error procesando OutboxEvent {}: {}", evento.id(), e.getMessage(), e);
                outboxPort.marcarError(evento.id(), e.getMessage());
            }
        }
    }

    private void procesarAlertaCritica(OutboxDispatcherPort.OutboxEventView evento) {
        ReoptimizarRutaCommand cmd = new ReoptimizarRutaCommand(
                evento.tenantId(),
                evento.aggregateId(),
                List.of(),   // unidades — vacío en dispatch automático (MVP)
                List.of(),   // zonas — vacío en dispatch automático (MVP)
                List.of(),   // alertasCriticas
                null,        // parametrosSolver — usa defaults del handler
                "ALERTA_CRITICA"
        );
        Result<?> resultado = reoptimizarUseCase.reoptimizar(cmd);
        if (resultado.isSuccess()) {
            outboxPort.marcarPublicado(evento.id(), Instant.now());
            log.info("OutboxEvent {} procesado: ruta {} reoptimizada", evento.id(), evento.aggregateId());
        } else {
            String msg = resultado.getError().message();
            outboxPort.marcarError(evento.id(), msg);
            log.warn("OutboxEvent {} falló reoptimización: {}", evento.id(), msg);
        }
    }
}
