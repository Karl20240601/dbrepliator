package io.microsphere.spring.db.event;

import io.microsphere.spring.db.event.sqlmetadata.PreparedStatementSqlMetaData;
import io.microsphere.spring.db.support.QueryBindings;
import io.microsphere.spring.db.support.ValueOperation;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class PreparedStatementContext extends StatementContext implements ValueOperation {
    private boolean autoCommit;
    private QueryBindings currentQueryBindings;
    private PreparedStatementSqlMetaData preparedStatementSqlMetaData;

    public PreparedStatementContext(String sql) {
        super();
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
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) {
        currentQueryBindings.setBindValue(parameterIndex, null, null);
    }

    @Override
    public void setByte(int parameterIndex, byte x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);

    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setString(int parameterIndex, String x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);

    }

    @Override
    public void setDate(int parameterIndex, Date x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setObject(int parameterIndex, Object x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) {
        currentQueryBindings.setBindValue(parameterIndex, null, reader);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);

    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);

    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) {
        currentQueryBindings.setBindValue(parameterIndex, null, null);

    }

    @Override
    public void setURL(int parameterIndex, URL x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);

    }

    @Override
    public void setRowId(int parameterIndex, RowId x) {
        currentQueryBindings.setBindValue(parameterIndex, null, x);

    }

    @Override
    public void setNString(int parameterIndex, String value) {
        currentQueryBindings.setBindValue(parameterIndex, null, value);

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) {
        currentQueryBindings.setBindValue(parameterIndex, null, value);

    }

    @Override
    public void setNClob(int parameterIndex, NClob value) {
        currentQueryBindings.setBindValue(parameterIndex, null, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) {
        currentQueryBindings.setBindValue(parameterIndex, null, reader);

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) {

    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader) {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) {

    }
}
