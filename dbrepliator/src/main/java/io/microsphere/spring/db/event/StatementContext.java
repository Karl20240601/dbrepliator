package io.microsphere.spring.db.event;

import io.microsphere.spring.db.event.sqlmetadata.StatementSqlMetaData;

public class StatementContext {
    private StatementSqlMetaData statementSqlMetaData;
    private String dataSourceBeanName;

    public StatementContext() {
        this.statementSqlMetaData = new StatementSqlMetaData();
    }

    public StatementContext(String sql) {
        appendSql(sql);
    }
    
    private void appendSql(String sql) {
        statementSqlMetaData.addBatch(sql);
    }

    public boolean isBatchUpdate() {
        return this.statementSqlMetaData.getBatchArgs().size() > 1;
    }

    public StatementSqlMetaData getStatementSqlMetaData() {
        return statementSqlMetaData;
    }

    public String getDataSourceBeanName() {
        return dataSourceBeanName;
    }

    public void setDataSourceBeanName(String dataSourceBeanName) {
        this.dataSourceBeanName = dataSourceBeanName;
    }
}
