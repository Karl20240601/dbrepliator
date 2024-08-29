package io.microsphere.spring.db.support.wrapper;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public  abstract class AbstractListenUpdateAbleSqlSession implements SqlSession {
    private final SqlSession sqlSessionDelegate;

    public AbstractListenUpdateAbleSqlSession(SqlSession sqlSessionDelegate) {
        this.sqlSessionDelegate = sqlSessionDelegate;
    }

    protected SqlSession getSqlSessionDelegate() {
        return sqlSessionDelegate;
    }

    @Override
    public <T> T selectOne(String statement) {
        return sqlSessionDelegate.selectOne(statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return sqlSessionDelegate.selectOne(statement,parameter);
    }

    @Override
    public <E> List<E> selectList(String statement) {
        return sqlSessionDelegate.selectList(statement);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
        return sqlSessionDelegate.selectList(statement,parameter);
    }

    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
        return sqlSessionDelegate.selectList(statement,parameter,rowBounds);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
        return sqlSessionDelegate.selectMap(statement,mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
        return sqlSessionDelegate.selectMap(statement,mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
        return sqlSessionDelegate.selectMap(statement,parameter,mapKey,rowBounds);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement) {
        return sqlSessionDelegate.selectCursor(statement);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter) {
        return sqlSessionDelegate.selectCursor(statement,parameter);
    }

    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
        return sqlSessionDelegate.selectCursor(statement,parameter,rowBounds);
    }

    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
         sqlSessionDelegate.select(statement,parameter,handler);
    }

    @Override
    public void select(String statement, ResultHandler handler) {
        sqlSessionDelegate.select(statement,handler);

    }

    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
        sqlSessionDelegate.select(statement,parameter,rowBounds,handler);
    }

    @Override
    public int insert(String statement) {
        return update(statement);
    }

    @Override
    public int insert(String statement, Object parameter) {
        return update(statement,parameter);
    }



    @Override
    public int delete(String statement) {
        return update(statement);
    }

    @Override
    public int delete(String statement, Object parameter) {
        return update(statement,parameter);
    }



    @Override
    public List<BatchResult> flushStatements() {
        return  sqlSessionDelegate.flushStatements();
    }

    @Override
    public void clearCache() {
        sqlSessionDelegate.clearCache();
    }

    @Override
    public Configuration getConfiguration() {
        return sqlSessionDelegate.getConfiguration();
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return sqlSessionDelegate.getMapper(type);
    }

    @Override
    public Connection getConnection() {
        return sqlSessionDelegate.getConnection();
    }

}
