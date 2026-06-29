package pe.edu.unmsm.ciudadsana.rutas.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.List;

public interface RutasEventPublisherPort {
    void publishAll(List<DomainEvent> events);
}
