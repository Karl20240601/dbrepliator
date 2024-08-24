package io.microsphere.spring.db.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class BatchDbDataExecuteUpdateEvent extends ApplicationEvent implements IDataExecuteUpdateEvent {
    private static final long serialVersionUID = -4867312420535983951L;

    private String dataSourceBeanName;

    private final List<DbDataExecuteUpdateEventData> updateEvents = new ArrayList<>(0);

    public BatchDbDataExecuteUpdateEvent(Object source) {
        super(source);
    }

    public List<DbDataExecuteUpdateEventData> getUpdateEvents() {
        return updateEvents;
    }

    public void setUpdateEvents(List<DbDataExecuteUpdateEventData> updateEvents) {
        this.updateEvents.addAll(updateEvents);
    }

    @Override
    public String getDataSourceBeanName() {
        return dataSourceBeanName;
    }

    public void setDataSourceBeanName(String dataSourceBeanName) {
        this.dataSourceBeanName = dataSourceBeanName;
    }


    @Override
    public String messageKey() {
        if (CollectionUtils.isEmpty(updateEvents)) {
            return null;
        }
        return getUpdateEvents().get(0).getMessageKey();
    }

    @Override
    public boolean isBatch() {
        return true;
    }

    @Override
    public Object getEventData() {
        return updateEvents;
    }


}
