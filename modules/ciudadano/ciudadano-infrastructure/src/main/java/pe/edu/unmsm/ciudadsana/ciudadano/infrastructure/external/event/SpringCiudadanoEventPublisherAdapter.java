package pe.edu.unmsm.ciudadsana.ciudadano.infrastructure.external.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.ciudadano.application.port.out.CiudadanoEventPublisherPort;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.List;

@Component
public class SpringCiudadanoEventPublisherAdapter implements CiudadanoEventPublisherPort {

    private final ApplicationEventPublisher publisher;

    public SpringCiudadanoEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(publisher::publishEvent);
    }
}
