package io.microsphere.spring.db.support.event;

import io.microsphere.spring.db.support.wrapper.SqlSessionContext;
import org.springframework.context.ApplicationEvent;

public class DbDataUpdateEvent extends ApplicationEvent {
    private SqlSessionContext sqlSessionContext;
    private String messageKey;
    private boolean autoCommit;
    private Boolean commitForce;

    public DbDataUpdateEvent(Object source) {
        super(source);
    }

    public SqlSessionContext getSqlSessionContext() {
        return sqlSessionContext;
    }

    public void setSqlSessionContext(SqlSessionContext sqlSessionContext) {
        this.sqlSessionContext = sqlSessionContext;
    }

    public String getBeanName() {
        return sqlSessionContext.getSqlSessionFactorybeanName();
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Boolean getCommitForce() {
        return commitForce;
    }

    public void setCommitForce(Boolean commitForce) {
        this.commitForce = commitForce;
    }
}
