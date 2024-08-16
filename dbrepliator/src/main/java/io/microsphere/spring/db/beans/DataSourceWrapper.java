package io.microsphere.spring.db.beans;

import io.microsphere.spring.db.config.DBReplicatorConfiguration;
import io.microsphere.spring.db.support.ConnectionWrapper;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class DataSourceWrapper implements DataSource {
    private DataSource delegate;
    private DBReplicatorConfiguration dbReplicatorConfiguration;
    public DataSourceWrapper(DataSource delegate,DBReplicatorConfiguration dbReplicatorConfiguration) {
        this.delegate = delegate;
        this.dbReplicatorConfiguration = dbReplicatorConfiguration;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = delegate.getConnection();
        return new ConnectionWrapper(connection,dbReplicatorConfiguration);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection =  delegate.getConnection(username,password);
        return new ConnectionWrapper(connection,dbReplicatorConfiguration);

    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return delegate.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return delegate.isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return delegate.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        delegate.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        delegate.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return delegate.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return delegate.getParentLogger();
    }
}
