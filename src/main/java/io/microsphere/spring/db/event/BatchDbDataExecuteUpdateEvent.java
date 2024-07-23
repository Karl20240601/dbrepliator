package io.microsphere.spring.db.event;

import io.microsphere.spring.db.support.SqlParameter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class BatchDbDataExecuteUpdateEvent extends ApplicationEvent {
    private final long timestamp = System.nanoTime();

    private int methodIndex;
    private String[] sql;
    private List<SqlParameter[]> parameters;

    public BatchDbDataExecuteUpdateEvent(Object source) {
        super(source);
    }


}
