package io.microsphere.spring.db.support;

import io.microsphere.spring.db.event.PreparedStatementContext;
import io.microsphere.spring.db.event.PreparedStatementEventListener;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {
    private final PreparedStatement statement;
    private final ConnectionWrapper connectionWrapper;
    private final PreparedStatementContext statementContext;
    private final PreparedStatementEventListener listeners;
    private final String sql;

    public PreparedStatementWrapper(PreparedStatement statement, String sql, ConnectionWrapper connectionWrapper, PreparedStatementEventListener listeners) {
        super(statement,connectionWrapper,listeners);
        this.statement = statement;
        this.connectionWrapper = connectionWrapper;
        this.statementContext = new PreparedStatementContext(sql);
        this.listeners = listeners;
        this.sql = sql;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }


    @Override
    public int executeUpdate() throws SQLException {
        int result = statement.executeUpdate();
        if (result <= 0) {
            return result;
        }
        listeners.onPreparedExecuteUpdate(statementContext);
        return result;
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        this.statement.setNull(parameterIndex,sqlType);
        statementContext.setNull(parameterIndex,sqlType);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        this.statement.setBoolean(parameterIndex,x);
        statementContext.setBoolean(parameterIndex,x);

    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        this.statement.setByte(parameterIndex,x);
        statementContext.setByte(parameterIndex,x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        this.statement.setShort(parameterIndex,x);
        statementContext.setShort(parameterIndex,x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        this.statement.setInt(parameterIndex,x);
        statementContext.setInt(parameterIndex,x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        this.statement.setLong(parameterIndex,x);
        statementContext.setLong(parameterIndex,x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        this.statement.setFloat(parameterIndex,x);
        statementContext.setFloat(parameterIndex,x);

    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        this.statement.setDouble(parameterIndex,x);
        statementContext.setDouble(parameterIndex,x);

    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        this.statement.setBigDecimal(parameterIndex,x);
        statementContext.setBigDecimal(parameterIndex,x);

    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        this.statement.setString(parameterIndex,x);
        statementContext.setString(parameterIndex,x);

    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        this.statement.setBytes(parameterIndex,x);
        statementContext.setBytes(parameterIndex,x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        this.statement.setDate(parameterIndex,x);
        statementContext.setDate(parameterIndex,x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        this.statement.setTime(parameterIndex,x);
        statementContext.setTime(parameterIndex,x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        this.statement.setTimestamp(parameterIndex,x);
        statementContext.setTimestamp(parameterIndex,x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.statement.setAsciiStream(parameterIndex,x,length);
        statementContext.setAsciiStream(parameterIndex,x,length);

    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.statement.setUnicodeStream(parameterIndex,x,length);
        statementContext.setUnicodeStream(parameterIndex,x,length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.statement.setBinaryStream(parameterIndex,x,length);
        statementContext.setBinaryStream(parameterIndex,x,length);
    }

    @Override
    public void clearParameters() throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        this.statement.setObject(parameterIndex,x,targetSqlType);
        statementContext.setObject(parameterIndex,x,targetSqlType);
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        this.statement.setObject(parameterIndex,x);
        statementContext.setObject(parameterIndex,x);
    }

    @Override
    public boolean execute() throws SQLException {
        return false;
    }

    @Override
    public void addBatch() throws SQLException {
        this.statement.addBatch();
        statementContext.addBatch();

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {

    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {

    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {

    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        statementContext.setDate(parameterIndex,x,cal);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        statementContext.setTime(parameterIndex,x,cal);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        statementContext.setTimestamp(parameterIndex,x,cal);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        statementContext.setNull(parameterIndex,sqlType,typeName);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        statementContext.setURL(parameterIndex,x);

    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return null;
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {

    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {

    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        statementContext.setObject(parameterIndex,x,targetSqlType,scaleOrLength);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {

    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {

    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {

    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {

    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {

    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {

    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {

    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return 0;
    }

    @Override
    public void close() throws SQLException {

    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {

    }

    @Override
    public int getMaxRows() throws SQLException {
        return 0;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {

    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {

    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return 0;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {

    }

    @Override
    public void cancel() throws SQLException {

    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void setCursorName(String name) throws SQLException {

    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return false;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return null;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return 0;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return false;
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {

    }

    @Override
    public int getFetchDirection() throws SQLException {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {

    }

    @Override
    public int getFetchSize() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return 0;
    }

    @Override
    public int getResultSetType() throws SQLException {
        return 0;
    }



    @Override
    public void clearBatch() throws SQLException {
        statement.clearBatch();
        this.statement.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return statement.executeBatch();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return statement.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return statement.getMoreResults();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return statement.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.executeUpdate(sql,autoGeneratedKeys);
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return statement.executeUpdate(sql,columnIndexes);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return statement.executeUpdate();
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return statement.execute(sql,autoGeneratedKeys);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return statement.execute(sql,columnIndexes);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return statement.execute(sql,columnNames);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return statement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return statement.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        statement.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return statement.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        statement.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return statement.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return statement.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return statement.isWrapperFor(iface);
    }
}
