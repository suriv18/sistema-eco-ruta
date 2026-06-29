package pe.edu.unmsm.ciudadsana.rutas.infrastructure.external.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.rutas.application.port.out.RutasEventPublisherPort;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.List;

@Component
public class SpringRutasEventPublisherAdapter implements RutasEventPublisherPort {

    private final ApplicationEventPublisher publisher;

    public SpringRutasEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(publisher::publishEvent);
    }
}
