package pe.edu.unmsm.ciudadsana.shared.kernel.domain.model;

import pe.edu.unmsm.ciudadsana.shared.kernel.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot<ID> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public abstract ID getId();

    protected void recordDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    public List<DomainEvent> peekDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
