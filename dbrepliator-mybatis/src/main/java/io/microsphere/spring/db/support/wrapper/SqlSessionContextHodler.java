package io.microsphere.spring.db.support.wrapper;


import java.util.List;

public class SqlSessionContextHodler {
    private static final ThreadLocal<SqlSessionContext> SQLEXECUTECONTEXT_HODLER = new ThreadLocal<>();

    public static void add(StatementParamer statementParamer) {
        getLocalSqlSessionContext().addStatementParamer(statementParamer);
    }

    public static SqlSessionContext getSqlSessionContext() {
        return SQLEXECUTECONTEXT_HODLER.get();
    }

    public static void reset() {
        SqlSessionContext sqlSessionContext = SQLEXECUTECONTEXT_HODLER.get();
        if (sqlSessionContext == null) {
            return;
        }
        SQLEXECUTECONTEXT_HODLER.set(null);
        List<StatementParamer> tatementParamers = sqlSessionContext.getTatementParamers();
        if (tatementParamers != null && !tatementParamers.isEmpty()) {
            tatementParamers.clear();
        }
    }

    private static SqlSessionContext getLocalSqlSessionContext() {
        SqlSessionContext sqlSessionContext = SQLEXECUTECONTEXT_HODLER.get();
        if (sqlSessionContext == null) {
            sqlSessionContext = new SqlSessionContext();
        }
        return sqlSessionContext;
    }

    public static void init(String sqlSessionFactorybeanName, Object[] value, boolean autoCommit) {
        SqlSessionContext localSqlSessionContext = getLocalSqlSessionContext();
        localSqlSessionContext.setSqlSessionFactorybeanName(sqlSessionFactorybeanName);
        localSqlSessionContext.setAutoCommit(autoCommit);
        localSqlSessionContext.setSqlSessionContructvalues(value);
    }
}
