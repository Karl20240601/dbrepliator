package io.microsphere.spring.db.support;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;

public interface ValueOperation {
//    void setBoolean(int parameterIndex, boolean x,BindValue[] bindValues);
//
//    void setNull(int parameterIndex, int sqlType,BindValue[] bindValues);
//
//    void setByte(int parameterIndex, byte x,BindValue[] bindValues);
//
//    void setShort(int parameterIndex, short x,BindValue[] bindValues);
//
//    void setInt(int parameterIndex, int x,BindValue[] bindValues);
//
//    void setLong(int parameterIndex, long x,BindValue[] bindValues);
//
//    void setFloat(int parameterIndex, float x,BindValue[] bindValues);
//
//    void setDouble(int parameterIndex, double x,BindValue[] bindValues);
//
//    void setBigDecimal(int parameterIndex, BigDecimal x,BindValue[] bindValues);
//
//    void setString(int parameterIndex, String x,BindValue[] bindValues);
//
//    void setBytes(int parameterIndex, byte x[],BindValue[] bindValues);
//
//    void setDate(int parameterIndex, java.sql.Date x,BindValue[] bindValues);
//
//    void setTime(int parameterIndex, java.sql.Time x,BindValue[] bindValues);
//
//    void setTimestamp(int parameterIndex, java.sql.Timestamp x,BindValue[] bindValues);
//
//    void setAsciiStream(int parameterIndex, java.io.InputStream x, int length,BindValue[] bindValues);
//
//    void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length,BindValue[] bindValues);
//
//    void setBinaryStream(int parameterIndex, java.io.InputStream x, int length,BindValue[] bindValues);
//
//    void setObject(int parameterIndex, Object x, int targetSqlType,BindValue[] bindValues);
//
//    void setObject(int parameterIndex, Object x,BindValue[] bindValues);
//
//
//    void setCharacterStream(int parameterIndex, java.io.Reader reader, int length,BindValue[] bindValues);
//
//    void setRef(int parameterIndex, Ref x,BindValue[] bindValues);
//
//    void setBlob(int parameterIndex, Blob x,BindValue[] bindValues);
//
//    void setClob(int parameterIndex, Clob x,BindValue[] bindValues);
//
//    void setArray(int parameterIndex, Array x,BindValue[] bindValues);
//
//    void setDate(int parameterIndex, java.sql.Date x, Calendar cal,BindValue[] bindValues);
//
//    void setTime(int parameterIndex, java.sql.Time x, Calendar cal,BindValue[] bindValues);
//
//    void setTimestamp(int parameterIndex, java.sql.Timestamp x, Calendar cal,BindValue[] bindValues);
//
//    void setNull(int parameterIndex, int sqlType, String typeName,BindValue[] bindValues);
//
//    void setURL(int parameterIndex, java.net.URL x,BindValue[] bindValues);
//
//    void setRowId(int parameterIndex, RowId x,BindValue[] bindValues);
//
//    void setNString(int parameterIndex, String value,BindValue[] bindValues);
//
//    void setNCharacterStream(int parameterIndex, Reader value, long length,BindValue[] bindValues);
//
//    void setNClob(int parameterIndex, NClob value,BindValue[] bindValues);
//
//    void setClob(int parameterIndex, Reader reader, long length,BindValue[] bindValues);
//
//    void setNClob(int parameterIndex, Reader reader, long length,BindValue[] bindValues);
//
//    void setSQLXML(int parameterIndex, SQLXML xmlObject,BindValue[] bindValues);
//
//    void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength,BindValue[] bindValues);
//
//    void setAsciiStream(int parameterIndex, java.io.InputStream x, long length,BindValue[] bindValues);
//
//    void setBinaryStream(int parameterIndex, java.io.InputStream x,
//                         long length,BindValue[] bindValues);
//
//    void setCharacterStream(int parameterIndex,
//                            java.io.Reader reader,
//                            long length,BindValue[] bindValues);
//
//    void setAsciiStream(int parameterIndex, java.io.InputStream x,BindValue[] bindValues);
//
//    void setBinaryStream(int parameterIndex, java.io.InputStream x,BindValue[] bindValues);
//
//    void setCharacterStream(int parameterIndex,
//                            java.io.Reader reader,BindValue[] bindValues);
//
//    void setNCharacterStream(int parameterIndex, Reader value,BindValue[] bindValues);
//
//    void setClob(int parameterIndex, Reader reader);
//
//    void setBlob(int parameterIndex, InputStream inputStream);
//
//    void setNClob(int parameterIndex, Reader reader);

    void setBoolean(int parameterIndex, boolean x);

    void setNull(int parameterIndex, int sqlType);

    void setByte(int parameterIndex, byte x);

    void setShort(int parameterIndex, short x);

    void setInt(int parameterIndex, int x);

    void setLong(int parameterIndex, long x);

    void setFloat(int parameterIndex, float x);

    void setDouble(int parameterIndex, double x);

    void setBigDecimal(int parameterIndex, BigDecimal x);

    void setString(int parameterIndex, String x);

    void setBytes(int parameterIndex, byte x[]);

    void setDate(int parameterIndex, java.sql.Date x);

    void setTime(int parameterIndex, java.sql.Time x);

    void setTimestamp(int parameterIndex, java.sql.Timestamp x);

    void setAsciiStream(int parameterIndex, java.io.InputStream x, int length);

    void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length);

    void setBinaryStream(int parameterIndex, java.io.InputStream x, int length);

    void setObject(int parameterIndex, Object x, int targetSqlType);

    void setObject(int parameterIndex, Object x);


    void setCharacterStream(int parameterIndex, java.io.Reader reader, int length);

    void setRef(int parameterIndex, Ref x);

    void setBlob(int parameterIndex, Blob x);

    void setClob(int parameterIndex, Clob x);

    void setArray(int parameterIndex, Array x);

    void setDate(int parameterIndex, java.sql.Date x, Calendar cal);

    void setTime(int parameterIndex, java.sql.Time x, Calendar cal);

    void setTimestamp(int parameterIndex, java.sql.Timestamp x, Calendar cal);

    void setNull(int parameterIndex, int sqlType, String typeName);

    void setURL(int parameterIndex, java.net.URL x);

    void setRowId(int parameterIndex, RowId x);

    void setNString(int parameterIndex, String value);

    void setNCharacterStream(int parameterIndex, Reader value, long length);

    void setNClob(int parameterIndex, NClob value);

    void setClob(int parameterIndex, Reader reader, long length);

    void setNClob(int parameterIndex, Reader reader, long length);

    void setSQLXML(int parameterIndex, SQLXML xmlObject);

    void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength);

    void setAsciiStream(int parameterIndex, java.io.InputStream x, long length);

    void setBinaryStream(int parameterIndex, java.io.InputStream x,
                         long length);

    void setCharacterStream(int parameterIndex,
                            java.io.Reader reader,
                            long length);

    void setAsciiStream(int parameterIndex, java.io.InputStream x);

    void setBinaryStream(int parameterIndex, java.io.InputStream x);

    void setCharacterStream(int parameterIndex,
                            java.io.Reader reader);

    void setNCharacterStream(int parameterIndex, Reader value);

    void setClob(int parameterIndex, Reader reader);

    void setBlob(int parameterIndex, InputStream inputStream);

    void setNClob(int parameterIndex, Reader reader);
}
