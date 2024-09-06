package io.microsphere.spring.db.support.event;

import io.microsphere.spring.db.support.wrapper.SqlSessionContext;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.UUID;

public class DbDataUpdateEvent extends ApplicationEvent implements Serializable {
    private static final long serialVersionUID = -7626394850479581275L;
    private SqlSessionContext sqlSessionContext;
    private String messageKey= UUID.randomUUID().toString();

    public DbDataUpdateEvent(Object source) {
        super(source);
        this.sqlSessionContext = (SqlSessionContext)(source);

    }

    public DbDataUpdateEvent() {
        super(new Object());
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

}
