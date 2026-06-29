package pe.edu.unmsm.ciudadsana.operacion.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.List;

public interface OperacionEventPublisherPort {
    void publishAll(List<DomainEvent> events);
}
