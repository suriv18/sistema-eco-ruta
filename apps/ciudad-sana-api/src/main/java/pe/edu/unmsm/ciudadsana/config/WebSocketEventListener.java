package pe.edu.unmsm.ciudadsana.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.domain.event.AlertaRegistradaEvent;
import pe.edu.unmsm.ciudadsana.rutas.domain.event.RutaEstadoCambiadoEvent;
import pe.edu.unmsm.ciudadsana.rutas.domain.event.RutaVersionAgregadaEvent;
import pe.edu.unmsm.ciudadsana.telemetria.domain.event.DesvioRutaDetectadoEvent;
import pe.edu.unmsm.ciudadsana.telemetria.domain.event.PingGpsRecibidoEvent;

import java.util.Map;

/**
 * Escucha los domain events de Spring y los reenvía a los topics STOMP correspondientes.
 * <p>
 * NOTA: AlertaCriticaDetectadaEvent no existe en ciudadano-domain (sprint2-task4-brief.md).
 * Se usa AlertaRegistradaEvent como proxy para el topic /topic/alerta.critica.recibida.
 * Cuando se implemente AlertaCriticaDetectadaEvent, reemplazar el handler onAlertaCritica.
 */
@Component
public class WebSocketEventListener {

    private final SimpMessagingTemplate messaging;

    public WebSocketEventListener(SimpMessagingTemplate messaging) {
        this.messaging = messaging;
    }

    /**
     * Publica la posición actualizada de una unidad en el topic STOMP.
     * Topic: /topic/unidad.posicion.actualizada
     */
    @EventListener
    public void onPingGps(PingGpsRecibidoEvent event) {
        Map<String, Object> payload = Map.of(
                "unidadExternoId", event.unidadExternoId(),
                "latitud", event.latitud(),
                "longitud", event.longitud(),
                "tenantId", event.tenantId()
        );
        messaging.convertAndSend("/topic/unidad.posicion.actualizada", (Object) payload);
    }

    /**
     * Proxy para "unidad sin señal" — usa DesvioRutaDetectadoEvent como evento de anomalía más cercano.
     * Topic: /topic/unidad.sin.senal
     */
    @EventListener
    public void onDesvioRuta(DesvioRutaDetectadoEvent event) {
        Map<String, Object> payload = Map.of(
                "agregadoId", event.aggregateId(),
                "unidadExternoId", event.unidadExternoId(),
                "rutaExternoId", event.rutaExternoId(),
                "distanciaDesvioM", event.distanciaDesvioM(),
                "tenantId", event.tenantId()
        );
        messaging.convertAndSend("/topic/unidad.sin.senal", (Object) payload);
    }

    /**
     * Proxy para alerta crítica — usa AlertaRegistradaEvent mientras no exista AlertaCriticaDetectadaEvent.
     * Topic: /topic/alerta.critica.recibida
     * TODO: reemplazar por AlertaCriticaDetectadaEvent cuando se implemente en ciudadano-domain.
     */
    @EventListener
    public void onAlertaCritica(AlertaRegistradaEvent event) {
        Map<String, Object> payload = Map.of(
                "alertaId", event.aggregateId(),
                "distritoExternoId", event.distritoExternoId(),
                "titulo", event.titulo(),
                "tenantId", event.tenantId()
        );
        messaging.convertAndSend("/topic/alerta.critica.recibida", (Object) payload);
    }

    /**
     * Publica la versión nueva de una ruta en el topic STOMP.
     * Topic: /topic/ruta.actualizada
     */
    @EventListener
    public void onRutaActualizada(RutaVersionAgregadaEvent event) {
        Map<String, Object> payload = Map.of(
                "rutaId", event.aggregateId(),
                "version", event.version(),
                "motivo", event.motivo(),
                "tenantId", event.tenantId()
        );
        messaging.convertAndSend("/topic/ruta.actualizada", (Object) payload);
    }

    /**
     * Publica el cambio de estado de una ruta en el topic STOMP.
     * Topic: /topic/ruta.estado.cambiado
     */
    @EventListener
    public void onRutaEstadoCambiado(RutaEstadoCambiadoEvent event) {
        Map<String, Object> payload = Map.of(
                "rutaId", event.aggregateId(),
                "estadoAnterior", event.estadoAnterior(),
                "estadoNuevo", event.estadoNuevo(),
                "tenantId", event.tenantId()
        );
        messaging.convertAndSend("/topic/ruta.estado.cambiado", (Object) payload);
    }
}
