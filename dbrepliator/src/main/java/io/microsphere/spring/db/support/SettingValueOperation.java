package io.microsphere.spring.db.support;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public interface SettingValueOperation {


    default void setBoolean(int parameterIndex, boolean x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, boolean.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setNull(int parameterIndex, int sqlType, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(null, null, null);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setByte(int parameterIndex, byte x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, byte.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setShort(int parameterIndex, short x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, short.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setInt(int parameterIndex, int x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, int.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setLong(int parameterIndex, long x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, long.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setFloat(int parameterIndex, float x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, float.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setDouble(int parameterIndex, double x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, double.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setBigDecimal(int parameterIndex, BigDecimal x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, BigDecimal.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setString(int parameterIndex, String x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, String.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setBytes(int parameterIndex, byte[] x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, byte[].class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setDate(int parameterIndex, Date x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, Date.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setTime(int parameterIndex, Time x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, Time.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setTimestamp(int parameterIndex, Timestamp x, SqlParameter[] sqlParameters) {
        SqlParameter sqlParameter = new SqlParameter(x, null, Timestamp.class);
        sqlParameters[parameterIndex] = sqlParameter;
    }


    default void setAsciiStream(int parameterIndex, InputStream x, int length, SqlParameter[] sqlParameters) {

    }


    default void setUnicodeStream(int parameterIndex, InputStream x, int length, SqlParameter[] sqlParameters) {

    }


    default void setBinaryStream(int parameterIndex, InputStream x, int length, SqlParameter[] sqlParameters) {

    }


    default void setObject(int parameterIndex, Object x, int targetSqlType, SqlParameter[] sqlParameters) {

    }


    default void setObject(int parameterIndex, Object x, SqlParameter[] sqlParameters) {

    }


    default void setCharacterStream(int parameterIndex, Reader reader, int length, SqlParameter[] sqlParameters) {

    }


    default void setRef(int parameterIndex, Ref x, SqlParameter[] sqlParameters) {

    }


    default void setBlob(int parameterIndex, Blob x, SqlParameter[] sqlParameters) {

    }


    default void setClob(int parameterIndex, Clob x, SqlParameter[] sqlParameters) {

    }


    default void setArray(int parameterIndex, Array x, SqlParameter[] sqlParameters) {

    }


    default void setDate(int parameterIndex, Date x, Calendar cal, SqlParameter[] sqlParameters) {

    }


    default void setTime(int parameterIndex, Time x, Calendar cal, SqlParameter[] sqlParameters) {

    }


    default void setTimestamp(int parameterIndex, Timestamp x, Calendar cal, SqlParameter[] sqlParameters) {

    }


    default void setNull(int parameterIndex, int sqlType, String typeName, SqlParameter[] sqlParameters) {

    }


    default void setURL(int parameterIndex, URL x, SqlParameter[] sqlParameters) {

    }


    default void setRowId(int parameterIndex, RowId x, SqlParameter[] sqlParameters) {

    }


    default void setNString(int parameterIndex, String value, SqlParameter[] sqlParameters) {

    }


    default void setNCharacterStream(int parameterIndex, Reader value, long length, SqlParameter[] sqlParameters) {

    }


    default void setNClob(int parameterIndex, NClob value, SqlParameter[] sqlParameters) {

    }


    default void setClob(int parameterIndex, Reader reader, long length, SqlParameter[] sqlParameters) {

    }


    default void setNClob(int parameterIndex, Reader reader, long length, SqlParameter[] sqlParameters) {

    }


    default void setSQLXML(int parameterIndex, SQLXML xmlObject, SqlParameter[] sqlParameters) {

    }


    default void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength, SqlParameter[] sqlParameters) {

    }


    default void setAsciiStream(int parameterIndex, InputStream x, long length, SqlParameter[] sqlParameters) {

    }


    default void setBinaryStream(int parameterIndex, InputStream x, long length, SqlParameter[] sqlParameters) {

    }


    default void setCharacterStream(int parameterIndex, Reader reader, long length, SqlParameter[] sqlParameters) {

    }


    default void setAsciiStream(int parameterIndex, InputStream x, SqlParameter[] sqlParameters) {

    }


    default void setBinaryStream(int parameterIndex, InputStream x, SqlParameter[] sqlParameters) {

    }


    default void setCharacterStream(int parameterIndex, Reader reader, SqlParameter[] sqlParameters) {

    }


    default void setNCharacterStream(int parameterIndex, Reader value, SqlParameter[] sqlParameters) {

    }


    default void setClob(int parameterIndex, Reader reader) {

    }


    default void setBlob(int parameterIndex, InputStream inputStream) {

    }


    default void setNClob(int parameterIndex, Reader reader) {

    }
}
