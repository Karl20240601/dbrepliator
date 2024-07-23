package io.microsphere.spring.db.support;

import io.microsphere.spring.db.serialize.EncoderAndDecoders;
import io.microsphere.spring.db.serialize.NullParameterValueEncoderAndDecoder;
import io.microsphere.spring.db.serialize.ParameterValueEncoderAndDecoder;

import java.lang.reflect.Type;
import java.sql.SQLType;

public class SqlParameter {
    private final Object value;
    private final byte[] rawValue;
    private final SQLType sqlType;
    private final Class javaType;
    private final ParameterValueEncoderAndDecoder parameterValueEncoderAndDecoder;

    public SqlParameter(Object value, SQLType sqlType, Class javaType) {
        this.value = value;
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.parameterValueEncoderAndDecoder = EncoderAndDecoders.getParameterValueEncoderAndDecoder(javaType == null ? null : javaType.getName());
        this.rawValue = parameterValueEncoderAndDecoder.getBytes(value);
    }

    private Object getValue() {
        return value;
    }

    ;

    private SQLType getSqlType() {
        return sqlType;
    }

    private Type getJavaType() {
        return javaType;
    }

    public byte[] getRawValue() {
        return rawValue;
    }

}
