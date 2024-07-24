package io.microsphere.spring.db.support;

import io.microsphere.spring.db.event.PreparedStatementContext;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ContextUtils {
    private static final ThreadLocal<Map<Connection, PreparedStatementContext>> PREPAREDSTATEMENTCONTEXT_HOLDER = new ThreadLocal<>();

    public static PreparedStatementContext getPreparedStatementContext(Connection connection) {
        return PREPAREDSTATEMENTCONTEXT_HOLDER.get().get(connection);
    }

    public static void setPreparedStatementContext(Connection connection, PreparedStatementContext preparedStatementContext) {
        Map<Connection, PreparedStatementContext> connectionPreparedStatementContextMap = PREPAREDSTATEMENTCONTEXT_HOLDER.get();
        if (connectionPreparedStatementContextMap == null) {
            connectionPreparedStatementContextMap = new HashMap<>();
        }
        connectionPreparedStatementContextMap.put(connection, preparedStatementContext);
    }


    public static void clearContext() {
        Map<Connection, PreparedStatementContext> connectionPreparedStatementContextMap = PREPAREDSTATEMENTCONTEXT_HOLDER.get();
        if (connectionPreparedStatementContextMap != null) {
            connectionPreparedStatementContextMap.clear();
        }
    }

}
