package io.microsphere.spring.db.event;


import io.microsphere.spring.db.support.ConnectionWrapper;


public class ContextHodler {
    private static final ThreadLocal<TransactionContext> SQLEXECUTECONTEXT_HODLER = new ThreadLocal<>();

    public static void addSqlExecuteContext(ConnectionWrapper connection, ConnectionContext connectionContext) {
        TransactionContext transactionContext = SQLEXECUTECONTEXT_HODLER.get();
        if (transactionContext == null) {
            throw new RuntimeException("");
        }
        transactionContext.addConnectionContext(connection, connectionContext);
    }

    public static void addPreparedStatement(ConnectionWrapper connection, PreparedStatementContext preparedStatementContext) {
        TransactionContext transactionContext = SQLEXECUTECONTEXT_HODLER.get();
        if (transactionContext == null) {
            throw new RuntimeException("");
        }
        transactionContext.getConnectionContext(connection).addStatementContexts(preparedStatementContext);
    }

    public static TransactionContext getSqlExecuteContext() {
        return SQLEXECUTECONTEXT_HODLER.get();
    }

    public static ConnectionContext getConnectionContext(ConnectionWrapper connection) {
        return SQLEXECUTECONTEXT_HODLER.get().getConnectionContext(connection);
    }

    /**
     * 清上下文信息，如果SqlExecuteContext中项都为空，则说明事务中所有连接都已经提交，清楚SqlExecuteContext
     * @param connection
     */
    public static void clear(ConnectionWrapper connection) {
        SQLEXECUTECONTEXT_HODLER.get().clearConnectionContext(connection);
        if (SQLEXECUTECONTEXT_HODLER.get().isEmpty()) {
            SQLEXECUTECONTEXT_HODLER.set(null);
        }
    }


}
