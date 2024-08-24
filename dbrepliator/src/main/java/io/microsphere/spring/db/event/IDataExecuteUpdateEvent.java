package io.microsphere.spring.db.event;

public interface IDataExecuteUpdateEvent  {
    String getDataSourceBeanName();
    String messageKey();
    long getTimestamp();
    Object getEventData();
    default boolean isBatch(){
        return false;
    }
}
