package io.microsphere.spring.db.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.sql.SQLException;

public class PreparedStatementEventListenerImpl implements PreparedStatementEventListener {
    public static final String BEAN_NAME = "preparedStatementEventListenerImpl";

    private static final Logger logger = LoggerFactory.getLogger(PreparedStatementEventListenerImpl.class);
    private ApplicationEventPublisher applicationEventPublisher;
    @Override
    public void onPreparedExecuteUpdate(PreparedStatementContext statementContext) {

        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = new DbDataExecuteUpdateEvent(statementContext);
        try {
            if (statementContext.isAutoCommit()) {
                applicationEventPublisher.publishEvent(dbDataExecuteUpdateEvent);
            }

        } catch (SQLException sqlException) {
            logger.error("链接可能被关闭", sqlException);
        }

    }

    @Override
    public void onexecuteBatch(StatementContext StatementContext) {

    }

    @Override
    public void onRollback(StatementContext StatementContext) {

    }

    @Override
    public void executeUpdate(StatementContext StatementContext) {

    }

    @Override
    public void onCommit(StatementContext statementContext) {

    }
}
