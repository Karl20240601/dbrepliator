package io.microsphere.spring.db.event.sqlmetadata;

import io.microsphere.spring.db.support.SqlParameter;

import java.io.Serializable;


public class PreparedStatementSqlMetaData extends AbstractSqlMetaData<SqlParameter[]> implements Serializable {
    private static final long serialVersionUID = 8002210106775008274L;
    private String executeSql;

    public PreparedStatementSqlMetaData(String executeSql) {
        this.executeSql = executeSql;
    }

    public PreparedStatementSqlMetaData() {

    }

    public String getExecuteSql() {
        return executeSql;
    }

    public void setExecuteSql(String executeSql) {
        this.executeSql = executeSql;
    }
}
