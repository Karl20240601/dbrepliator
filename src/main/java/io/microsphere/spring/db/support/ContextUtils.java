package io.microsphere.spring.db.support;

import io.microsphere.spring.db.event.PreparedStatementContext;

public class ContextUtils {
    private static final ThreadLocal<PreparedStatementContext> PREPAREDSTATEMENTCONTEXT_HOLDER = new ThreadLocal<>();

    public static PreparedStatementContext getPreparedStatementContext() {
        return PREPAREDSTATEMENTCONTEXT_HOLDER.get();
    }

    public static void setPreparedStatementContext(PreparedStatementContext preparedStatementContext) {
        PREPAREDSTATEMENTCONTEXT_HOLDER.set(preparedStatementContext);
    }

    public static void setAutoCommit(boolean autoCommit) {
        PreparedStatementContext preparedStatementContext = getPreparedStatementContext();
        if (preparedStatementContext == null) {
            return;
        }
        preparedStatementContext.setAutoCommit(autoCommit);
    }

    public static void clearContext() {
        setPreparedStatementContext(null);
    }

}
