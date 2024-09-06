package io.microsphere.spring.db.support.wrapper;

import io.microsphere.spring.db.config.MybatisContext;
import io.microsphere.spring.db.support.event.SqlSessionEventLlistener;
import org.apache.ibatis.session.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlSessionFactoryWrapper implements SqlSessionFactory {
    private final SqlSessionFactory sqlSessionFactory;
    private final String sqlSessionFactorybeanName;
    private final MybatisContext mybatisContext;

    public SqlSessionFactoryWrapper(SqlSessionFactory sqlSessionFactory, MybatisContext mybatisContext, String sqlSessionFactorybeanName) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.sqlSessionFactorybeanName = sqlSessionFactorybeanName;
        this.mybatisContext = mybatisContext;
    }

    @Override
    public SqlSession openSession() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        if (!mybatisContext.isEnable()) {
            return sqlSession;
        }
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, null, false);
        return new DbReplSqlSession(sqlSession, mybatisContext.getSqlSessionEventLlistener(), false);
    }

    @Override
    public SqlSession openSession(boolean autoCommit) {


        SqlSession sqlSession = sqlSessionFactory.openSession(autoCommit);
        if (!mybatisContext.isEnable()) {
            return sqlSession;
        }
        Object[] values = {autoCommit};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, autoCommit);
        return new DbReplSqlSession(sqlSession, mybatisContext.getSqlSessionEventLlistener(), autoCommit);
    }

    @Override
    public SqlSession openSession(Connection connection) {
        SqlSession sqlSession = sqlSessionFactory.openSession(connection);
        if (!mybatisContext.isEnable()) {
            return sqlSession;
        }

        Object[] values = {Connection.class};
        boolean autoCommit = getAutoCommit(connection);
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, autoCommit);
        return new DbReplSqlSession(sqlSession, mybatisContext.getSqlSessionEventLlistener(), autoCommit);
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
        SqlSession sqlSession = sqlSessionFactory.openSession(level);
        if (!mybatisContext.isEnable()) {
            return sqlSession;
        }

        Object[] values = {level};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, false);
        return new DbReplSqlSession(sqlSession, mybatisContext.getSqlSessionEventLlistener(), false);
    }

    @Override
    public SqlSession openSession(ExecutorType execType) {
        SqlSession sqlSession = sqlSessionFactory.openSession(execType);
        if (!mybatisContext.isEnable()) {
            return sqlSession;
        }

        Object[] values = {execType};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, false);
        return new DbReplSqlSession(sqlSession, mybatisContext.getSqlSessionEventLlistener(), false);
    }

    @Override
    public SqlSession openSession(ExecutorType execType, boolean autoCommit) {
        SqlSession sqlSession = sqlSessionFactory.openSession(execType);
        if (!mybatisContext.isEnable()) {
            return sqlSession;
        }

        Object[] values = {execType,autoCommit};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, autoCommit);
        return new DbReplSqlSession(sqlSession, mybatisContext.getSqlSessionEventLlistener(), autoCommit);
    }

    @Override
    public SqlSession openSession(ExecutorType execType, TransactionIsolationLevel level) {
        SqlSession sqlSession = sqlSessionFactory.openSession(execType, level);
        if (!mybatisContext.isEnable()) {
            return sqlSession;
        }

        Object[] values = {execType, level};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, false);
        return new DbReplSqlSession(sqlSession, mybatisContext.getSqlSessionEventLlistener(), false);
    }

    @Override
    public SqlSession openSession(ExecutorType execType, Connection connection) {
        SqlSession sqlSession = sqlSessionFactory.openSession(execType, connection);
        if (!mybatisContext.isEnable()) {
            return sqlSession;
        }

        Object[] values = {execType, Connection.class};
        SqlSessionContextHodler.init(sqlSessionFactorybeanName, values, getAutoCommit(connection));
        return new DbReplSqlSession(sqlSession, mybatisContext.getSqlSessionEventLlistener(), getAutoCommit(connection));
    }

    @Override
    public Configuration getConfiguration() {
        return sqlSessionFactory.getConfiguration();
    }
}
