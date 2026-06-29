package pe.edu.unmsm.ciudadsana.ciudadano.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.List;

public interface CiudadanoEventPublisherPort {
    void publishAll(List<DomainEvent> events);
}
