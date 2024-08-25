package io.microsphere.spring.db.event;

import io.microsphere.spring.db.event.sqlmetadata.PreparedStatementSqlMetaData;
import io.microsphere.spring.db.support.QueryBindings;
import io.microsphere.spring.db.support.ValueOperation;
import io.microsphere.spring.db.utils.StreamUtils;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

import static io.microsphere.spring.db.event.sqltypes.JdbcTypeUtils.getJDBCType;

public class PreparedStatementContext extends StatementContext implements ValueOperation {
    private boolean autoCommit;
    private QueryBindings currentQueryBindings;
    private PreparedStatementSqlMetaData preparedStatementSqlMetaData;
    public final int maxInpuStramSize;
    public final int maxCharSize;

    public PreparedStatementContext(String sql) {
        super();
        this.maxInpuStramSize = -1;
        this.maxCharSize = -1;
        PreparedStatementSqlMetaData preparedStatementSqlMetaData = new PreparedStatementSqlMetaData(sql);
    }

    public PreparedStatementContext(String sql, int maxInpuStramSize, int maxCharSize) {
        super();

        this.maxInpuStramSize = maxInpuStramSize;
        this.maxCharSize = maxCharSize;
        PreparedStatementSqlMetaData preparedStatementSqlMetaData = new PreparedStatementSqlMetaData(sql);
    }

    public void addBatch() {
        preparedStatementSqlMetaData.addBatch(currentQueryBindings.cloneBindValue());
        currentQueryBindings.clearBindValues();
    }

    public PreparedStatementSqlMetaData getPreparedStatementSqlMetaData() {
        return preparedStatementSqlMetaData;
    }

    public void execute() {
        preparedStatementSqlMetaData.addBatch(currentQueryBindings.cloneBindValue());
        currentQueryBindings.clearBindValues();
    }

    public boolean isAutoCommit() throws SQLException {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Boolean.class), x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) {
        currentQueryBindings.setBindValue(parameterIndex, JDBCType.NULL, null);
    }

    @Override
    public void setByte(int parameterIndex, byte x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Byte.class), x);
    }

    @Override
    public void setShort(int parameterIndex, short x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Integer.class), x);
    }

    @Override
    public void setInt(int parameterIndex, int x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Integer.class), x);
    }

    @Override
    public void setLong(int parameterIndex, long x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Long.class), x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Float.class), x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Double.class), x);

    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(BigDecimal.class), x);
    }

    @Override
    public void setString(int parameterIndex, String x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(String.class), x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(byte[].class), x);

    }

    @Override
    public void setDate(int parameterIndex, Date x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Date.class), x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) {
        processIOParameter(parameterIndex,reader,length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);

    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(sqlType), null);

    }

    @Override
    public void setURL(int parameterIndex, URL x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);

    }

    @Override
    public void setRowId(int parameterIndex, RowId x) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(x.getClass()), x);

    }

    @Override
    public void setNString(int parameterIndex, String value) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(value.getClass()), value);

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(value.getClass()), value);

    }

    @Override
    public void setNClob(int parameterIndex, NClob value) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(value.getClass()), value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) {
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(InputStream.class), reader);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) {
        processIOParameter(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) {
        try {
            setCharacterStream(parameterIndex, xmlObject.getCharacterStream());
        } catch (SQLException e) {

        }
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) {
        processIOParameter(parameterIndex, x, length);

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) {
        processIOParameter(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) {
        processIOParameter(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) {
        processIOParameter(parameterIndex, x, this.maxInpuStramSize);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) {
        processIOParameter(parameterIndex, x, this.maxInpuStramSize);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) {
        processIOParameter(parameterIndex, reader, this.maxInpuStramSize);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) {
        processIOParameter(parameterIndex, value, this.maxInpuStramSize);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) {
        processIOParameter(parameterIndex, reader, this.maxInpuStramSize);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) {
        processIOParameter(parameterIndex, inputStream, this.maxInpuStramSize);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) {
        processIOParameter(parameterIndex, reader, this.maxInpuStramSize);
    }

    private int getInputStramLength(long length) {
        if (length > 0) {
            return (int) length;
        }

        if (this.maxInpuStramSize > 0) {
            return this.maxInpuStramSize;
        }

        return StreamUtils.INPUTSTREAM_MAX_BUFFER_SIZE;
    }

    private int getInputCharLength(long length) {
        if (length > 0) {
            return (int) length;
        }

        if (this.maxCharSize > 0) {
            return this.maxCharSize;
        }

        return StreamUtils.CHAR_MAX_BUFFER_SIZE;
    }


    public void processIOParameter(int parameterIndex, InputStream inputStream, long length) {
        int inputStramLength = getInputStramLength(length);
        byte[] bytes = StreamUtils.streamToBytes(inputStream, inputStramLength);
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Reader.class), bytes);
    }

    public void processIOParameter(int parameterIndex, Reader reader, long length) {
        int inputStramLength = getInputCharLength(length);
        byte[] bytes = StreamUtils.streamToBytes(reader, inputStramLength);
        currentQueryBindings.setBindValue(parameterIndex, getJDBCType(Reader.class), bytes);
    }
}
