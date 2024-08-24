package io.microsphere.spring.db.event;

import io.microsphere.spring.db.event.sqlmetadata.SqlMetaData;
import io.microsphere.spring.db.support.enums.StatementEnum;

import java.io.Serializable;


public class DbDataExecuteUpdateEventData implements Serializable {


    private static final long serialVersionUID = 4394874979811153428L;
    private SqlMetaData sqlMetaData;
    private String messageKey;
    private String beanName;
    private StatementEnum statementEnum;
    private long eventTimestamp;


    public SqlMetaData getSqlMetaData() {
        return sqlMetaData;
    }

    public void setSqlMetaData(SqlMetaData sqlMetaData) {
        this.sqlMetaData = sqlMetaData;
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

    public StatementEnum getStatementEnum() {
        return statementEnum;
    }

    public void setStatementEnum(StatementEnum statementEnum) {
        this.statementEnum = statementEnum;
    }

    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }
}
