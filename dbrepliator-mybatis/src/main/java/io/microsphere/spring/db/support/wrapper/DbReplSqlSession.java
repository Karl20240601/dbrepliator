package io.microsphere.spring.db.support.wrapper;


import io.microsphere.spring.db.support.event.SqlSessionEventLlistener;
import org.apache.ibatis.session.SqlSession;


public class DbReplSqlSession extends AbstractListenUpdateAbleSqlSession {
    private final boolean autoCommit;
    private final SqlSessionEventLlistener sqlSessionEventLlistener;


    public DbReplSqlSession(SqlSession sqlSession, SqlSessionEventLlistener sqlSessionEventLlistener, boolean autoCommit) {
        super(sqlSession);
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
        if (isAutoCommit()) {
            sqlSessionEventLlistener.onUpdate(SqlSessionContextHodler.getSqlSessionContext());
        }
    }

    @Override
    public int update(String statement, Object parameter) {
        int update = getSqlSessionDelegate().update(statement, parameter);
        listening(statement, parameter);
        return update;
    }

    @Override
    public void commit() {
        try {
            getSqlSessionDelegate().commit();
            sqlSessionEventLlistener.onCommit(SqlSessionContextHodler.getSqlSessionContext());
        } finally {
            SqlSessionContextHodler.reset();
        }
    }

    @Override
    public void commit(boolean force) {
        try {
            getSqlSessionDelegate().commit(force);
            sqlSessionEventLlistener.onCommit(SqlSessionContextHodler.getSqlSessionContext());
        } finally {
            SqlSessionContextHodler.reset();
        }
    }

    @Override
    public void rollback() {
        try {
            getSqlSessionDelegate().rollback();
        } finally {
            SqlSessionContextHodler.reset();
        }
    }

    @Override
    public void rollback(boolean force) {
        try {
            getSqlSessionDelegate().rollback(force);
        } finally {
            SqlSessionContextHodler.reset();
        }
    }

    public boolean isAutoCommit() {
        return autoCommit;
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
