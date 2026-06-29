package pe.edu.unmsm.ciudadsana.telemetria.infrastructure.external.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;
import pe.edu.unmsm.ciudadsana.telemetria.application.port.out.TelemetriaEventPublisherPort;

import java.util.List;

@Component
public class SpringTelemetriaEventPublisherAdapter implements TelemetriaEventPublisherPort {

    private final ApplicationEventPublisher publisher;

    public SpringTelemetriaEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(publisher::publishEvent);
    }
}
