package pe.edu.unmsm.ciudadsana.auth.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.List;

public interface EventPublisherPort {
    void publish(DomainEvent event);
    void publishAll(List<DomainEvent> events);
}
