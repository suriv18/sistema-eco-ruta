package pe.edu.unmsm.ciudadsana.auth.infrastructure.external.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.auth.application.port.out.EventPublisherPort;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.List;

@Component
public class SpringEventPublisherAdapter implements EventPublisherPort {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(publisher::publishEvent);
    }
}
