package pe.edu.unmsm.ciudadsana.telemetria.application.port.out;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.List;

public interface TelemetriaEventPublisherPort {
    void publishAll(List<DomainEvent> events);
}
