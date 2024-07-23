package io.microsphere.spring.db.event;

import io.microsphere.spring.db.support.SqlParameter;
import io.microsphere.spring.db.utils.SqlStringUtils;
import org.springframework.context.ApplicationEvent;


public class DbDataExecuteUpdateEvent extends ApplicationEvent {

    private int eventType;
    private String sql;
    private SqlParameter[] parameters;
    private String messageKey;
    private String beanName;

    public DbDataExecuteUpdateEvent(Object source) {
        super(source);
        PreparedStatementContext preparedStatementContext = (PreparedStatementContext) source;
        this.eventType = preparedStatementContext.getMenthodIndex();
        this.sql = preparedStatementContext.getSql();
        this.messageKey = SqlStringUtils.findTableNameBysql(sql);
        this.parameters = preparedStatementContext.getSqlParameter();
    }


    public int getEventType() {
        return eventType;
    }

    public void setEventType(int methodIndex) {
        this.eventType = methodIndex;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public SqlParameter[] getParameters() {
        return parameters;
    }

    public void setParameters(SqlParameter[] parameters) {
        this.parameters = parameters;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
