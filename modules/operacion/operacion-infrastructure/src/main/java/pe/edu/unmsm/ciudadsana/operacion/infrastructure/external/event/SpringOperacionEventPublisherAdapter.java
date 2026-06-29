package pe.edu.unmsm.ciudadsana.operacion.infrastructure.external.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.operacion.application.port.out.OperacionEventPublisherPort;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.List;

@Component
public class SpringOperacionEventPublisherAdapter implements OperacionEventPublisherPort {

    private final ApplicationEventPublisher publisher;

    public SpringOperacionEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(publisher::publishEvent);
    }
}
