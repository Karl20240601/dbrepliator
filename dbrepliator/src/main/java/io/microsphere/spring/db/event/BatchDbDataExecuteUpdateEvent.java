package io.microsphere.spring.db.event;

import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;

public class BatchDbDataExecuteUpdateEvent extends ApplicationEvent {
    private static final long serialVersionUID = -4867312420535983951L;
    private final long timestamp = System.nanoTime();

    private final List<DbDataExecuteUpdateEvent> updateEvents = new ArrayList<>(0);

    public BatchDbDataExecuteUpdateEvent(Object source) {
        super(source);
    }

    public List<DbDataExecuteUpdateEvent> getUpdateEvents() {
        return updateEvents;
    }

    public void setUpdateEvents(List<DbDataExecuteUpdateEvent> updateEvents) {
        this.updateEvents.addAll(updateEvents);
    }
}
