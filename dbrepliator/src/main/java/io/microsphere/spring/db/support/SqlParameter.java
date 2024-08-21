package io.microsphere.spring.db.support;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.SQLType;

public class SqlParameter implements Serializable {
    private static final long serialVersionUID = -4514591364656713472L;

    private final Object value;
    private final SQLType sqlType;
    private final Class javaType;

    public SqlParameter(Object value, SQLType sqlType, Class javaType) {
        this.value = value;
        this.sqlType = sqlType;
        this.javaType = javaType;
    }

    public Object getValue() {
        return value;
    }
    public SQLType getSqlType() {
        return sqlType;
    }

    public Type getJavaType() {
        return javaType;
    }

}
