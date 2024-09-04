package io.microsphere.spring.db.support.wrapper;


import io.microsphere.spring.db.support.event.SqlSessionEventLlistener;
import org.apache.ibatis.session.SqlSession;


public class DbReplSqlSession extends AbstractListenUpdateAbleSqlSession {
    private final boolean autoCommit;
    private final String beanName;
    private final SqlSessionEventLlistener sqlSessionEventLlistener;


    public DbReplSqlSession(SqlSession sqlSession, String beanName, SqlSessionEventLlistener sqlSessionEventLlistener, boolean autoCommit) {
        super(sqlSession);
        this.beanName = beanName;
        this.autoCommit = autoCommit;
        this.sqlSessionEventLlistener = sqlSessionEventLlistener;
    }

    @Override
    public int update(String statement) {
        int update = getSqlSessionDelegate().update(statement);
        listening(statement);
        return update;
    }

    private void listening(String statement) {
        listening(statement, null);
    }

    private void listening(String statement, Object parameter) {
        StatementParamer statementParamer = StatementParamerFactory.create(statement, parameter);
        statementParamer.setStatement(statement);
        SqlSessionContextHodler.add(statementParamer);
        if (autoCommit) {
            sqlSessionEventLlistener.onUpdate(SqlSessionContextHodler.getSqlSessionContext());
        }
    }

    @Override
    public int update(String statement, Object parameter) {
        int update = getSqlSessionDelegate().update(statement,parameter);
        listening(statement, parameter);
        return update;
    }

    @Override
    public void commit() {
        getSqlSessionDelegate().commit();
        sqlSessionEventLlistener.onCommit(SqlSessionContextHodler.getSqlSessionContext());
    }

    @Override
    public void commit(boolean force) {
        getSqlSessionDelegate().commit(force);
        sqlSessionEventLlistener.onCommit(SqlSessionContextHodler.getSqlSessionContext());
    }

    @Override
    public void rollback() {
        sqlSessionEventLlistener.onRollback(SqlSessionContextHodler.getSqlSessionContext());

    }

    @Override
    public void rollback(boolean force) {
        getSqlSessionDelegate().commit(force);
        sqlSessionEventLlistener.onRollback(SqlSessionContextHodler.getSqlSessionContext());
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public String getBeanName() {
        return beanName;
    }

    @Override
    public void close() {
        try {
            getSqlSessionDelegate().close();
        } finally {
            SqlSessionContextHodler.reset();
        }
    }

}
