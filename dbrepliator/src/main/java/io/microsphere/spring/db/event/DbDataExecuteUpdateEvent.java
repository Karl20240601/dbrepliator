package io.microsphere.spring.db.event;

import org.springframework.context.ApplicationEvent;


public class DbDataExecuteUpdateEvent extends ApplicationEvent {

    private static final long serialVersionUID = 8425295379903255511L;

    private DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData;

    private String dataSourceBeanName;

    public DbDataExecuteUpdateEvent(Object source, DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData) {
        super(source);
        this.dbDataExecuteUpdateEventData = dbDataExecuteUpdateEventData;
    }


    public DbDataExecuteUpdateEventData getDbDataExecuteUpdateEventData() {
        return dbDataExecuteUpdateEventData;
    }

    public void setDbDataExecuteUpdateEventData(DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData) {
        this.dbDataExecuteUpdateEventData = dbDataExecuteUpdateEventData;
    }

    public String getDataSourceBeanName() {
        return dataSourceBeanName;
    }

    public void setDataSourceBeanName(String dataSourceBeanName) {
        this.dataSourceBeanName = dataSourceBeanName;
    }
}
