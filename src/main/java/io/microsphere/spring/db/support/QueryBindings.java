package io.microsphere.spring.db.support;


import io.microsphere.spring.db.utils.SqlStringUtils;

import java.sql.SQLType;

public class QueryBindings {
    private final SqlParameter[] sqlParameters;

    public QueryBindings(int placeholdersNums) {
        this.sqlParameters = new SqlParameter[placeholdersNums];
    }


    public QueryBindings(String sql) {
        int numberOfPlaceholders = SqlStringUtils.findNumberOfPlaceholders(sql);
        if (numberOfPlaceholders < 1) {
            this.sqlParameters = null;
            return;
        }
        this.sqlParameters = new SqlParameter[numberOfPlaceholders];
    }


    public void setBindValue(int arrayIndex, SQLType sqlType, Object value) {
        sqlParameters[arrayIndex] = new SqlParameter(value, sqlType, value == null ? null : value.getClass());
    }

    public SqlParameter[] cloneBindValue() {
        SqlParameter[] newValues = new SqlParameter[sqlParameters.length];
        System.arraycopy(sqlParameters, 0, newValues, 0, sqlParameters.length);
        return newValues;
    }

    public void clearBindValues() {
        if (sqlParameters == null) {
            return;
        }
        for (int i = 0; i <= sqlParameters.length; i++) {
            sqlParameters[i] = null;
        }
    }


}
