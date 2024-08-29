package io.microsphere.spring.db.support.wrapper;

import io.microsphere.spring.db.support.event.SqlSessionEventLlistener;
import org.apache.ibatis.session.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlSessionFactoryWrapper implements SqlSessionFactory {
    private final SqlSessionFactory sqlSessionFactory;
    private final SqlSessionEventLlistener sqlSessionEventLlistener;
    private final String sqlSessionFactorybeanName;


    public SqlSessionFactoryWrapper(SqlSessionFactory sqlSessionFactory, SqlSessionEventLlistener sqlSessionEventLlistener, String sqlSessionFactorybeanName) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlSessionFactorybeanName = sqlSessionFactorybeanName;
        this.sqlSessionEventLlistener = sqlSessionEventLlistener;
    }

    @Override
    public SqlSession openSession() {
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, null, false);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        return new DbReplSqlSession(sqlSession, this.sqlSessionFactorybeanName, sqlSessionEventLlistener, false);
    }

    @Override
    public SqlSession openSession(boolean autoCommit) {
        Object[] values = {autoCommit};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, autoCommit);

        SqlSession sqlSession = sqlSessionFactory.openSession(autoCommit);
        return new DbReplSqlSession(sqlSession, this.sqlSessionFactorybeanName, sqlSessionEventLlistener, autoCommit);
    }

    @Override
    public SqlSession openSession(Connection connection) {
        Object[] values = {Connection.class};
        boolean autoCommit = getAutoCommit(connection);
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, autoCommit);

        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        return new DbReplSqlSession(sqlSession, this.sqlSessionFactorybeanName, sqlSessionEventLlistener, autoCommit);
    }

    private boolean getAutoCommit(Connection connection) {
        boolean autoCommit;
        try {
            autoCommit = connection.getAutoCommit();
        } catch (SQLException e) {
            // Failover to true, as most poor drivers
            // or databases won't support transactions
            autoCommit = true;
        }
        return autoCommit;
    }

    @Override
    public SqlSession openSession(TransactionIsolationLevel level) {
        Object[] values = {level};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, false);
        SqlSession sqlSession = sqlSessionFactory.openSession(level);
        return new DbReplSqlSession(sqlSession, this.sqlSessionFactorybeanName, sqlSessionEventLlistener, false);
    }

    @Override
    public SqlSession openSession(ExecutorType execType) {
        Object[] values = {execType};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, false);
        SqlSession sqlSession = sqlSessionFactory.openSession(execType);
        return new DbReplSqlSession(sqlSession, this.sqlSessionFactorybeanName, sqlSessionEventLlistener, false);
    }

    @Override
    public SqlSession openSession(ExecutorType execType, boolean autoCommit) {
        Object[] values = {execType};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, autoCommit);

        SqlSession sqlSession = sqlSessionFactory.openSession(execType);
        return new DbReplSqlSession(sqlSession, this.sqlSessionFactorybeanName, sqlSessionEventLlistener, autoCommit);
    }

    @Override
    public SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level) {
        Object[] values = {execType, level};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, false);

        SqlSession sqlSession = sqlSessionFactory.openSession(execType, level);
        return new DbReplSqlSession(sqlSession, this.sqlSessionFactorybeanName, sqlSessionEventLlistener, false);
    }

    @Override
    public SqlSession openSession(ExecutorType execType, Connection connection) {
        Object[] values = {execType, Connection.class};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, getAutoCommit(connection));

        SqlSession sqlSession = sqlSessionFactory.openSession(execType, connection);
        return new DbReplSqlSession(sqlSession, this.sqlSessionFactorybeanName, sqlSessionEventLlistener, getAutoCommit(connection));
    }

    @Override
    public Configuration getConfiguration() {
        return sqlSessionFactory.getConfiguration();
    }
}
