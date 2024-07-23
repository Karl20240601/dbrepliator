//package io.microsphere.spring.db.support;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.io.Reader;
//import java.math.BigDecimal;
//import java.net.URL;
//import java.sql.*;
//import java.util.Calendar;
//
//public class DefaultValueOperation implements ValueOperation {
//
//    @Override
//    public void setBoolean(int parameterIndex, boolean x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, boolean.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setNull(int parameterIndex, int sqlType, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(null, null, null);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setByte(int parameterIndex, byte x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, byte.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setShort(int parameterIndex, short x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, short.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setInt(int parameterIndex, int x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, int.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setLong(int parameterIndex, long x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, long.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setFloat(int parameterIndex, float x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, float.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setDouble(int parameterIndex, double x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, double.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setBigDecimal(int parameterIndex, BigDecimal x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, BigDecimal.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setString(int parameterIndex, String x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, String.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setBytes(int parameterIndex, byte[] x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, byte[].class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setDate(int parameterIndex, Date x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, Date.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setTime(int parameterIndex, Time x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, Time.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setTimestamp(int parameterIndex, Timestamp x, BindValue[] bindValues) {
//        BindValue bindValue = new BindValue(x, null, Timestamp.class);
//        bindValues[parameterIndex] = bindValue;
//    }
//
//    @Override
//    public void setAsciiStream(int parameterIndex, InputStream x, int length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setUnicodeStream(int parameterIndex, InputStream x, int length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setBinaryStream(int parameterIndex, InputStream x, int length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setObject(int parameterIndex, Object x, int targetSqlType, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setObject(int parameterIndex, Object x, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setCharacterStream(int parameterIndex, Reader reader, int length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setRef(int parameterIndex, Ref x, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setBlob(int parameterIndex, Blob x, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setClob(int parameterIndex, Clob x, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setArray(int parameterIndex, Array x, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setDate(int parameterIndex, Date x, Calendar cal, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setTime(int parameterIndex, Time x, Calendar cal, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setNull(int parameterIndex, int sqlType, String typeName, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setURL(int parameterIndex, URL x, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setRowId(int parameterIndex, RowId x, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setNString(int parameterIndex, String value, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setNCharacterStream(int parameterIndex, Reader value, long length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setNClob(int parameterIndex, NClob value, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setClob(int parameterIndex, Reader reader, long length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setNClob(int parameterIndex, Reader reader, long length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setSQLXML(int parameterIndex, SQLXML xmlObject, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setAsciiStream(int parameterIndex, InputStream x, long length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setBinaryStream(int parameterIndex, InputStream x, long length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setCharacterStream(int parameterIndex, Reader reader, long length, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setAsciiStream(int parameterIndex, InputStream x, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setBinaryStream(int parameterIndex, InputStream x, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setCharacterStream(int parameterIndex, Reader reader, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setNCharacterStream(int parameterIndex, Reader value, BindValue[] bindValues) {
//
//    }
//
//    @Override
//    public void setClob(int parameterIndex, Reader reader) {
//
//    }
//
//    @Override
//    public void setBlob(int parameterIndex, InputStream inputStream) {
//
//    }
//
//    @Override
//    public void setNClob(int parameterIndex, Reader reader) {
//
//    }
//}
