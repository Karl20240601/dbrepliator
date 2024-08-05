package io.microsphere.spring.db.event;

import io.microsphere.spring.db.support.enums.StatementEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.sql.SQLException;

public class DataUpdateEventListenerImpl implements DataUpdateEventListener {
    public static final String BEAN_NAME = "preparedStatementEventListenerImpl";

    private static final Logger logger = LoggerFactory.getLogger(DataUpdateEventListenerImpl.class);
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onPreparedExecuteUpdate(PreparedStatementContext statementContext) {
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = DbDataExecuteUpdateEventFactory.createDbDataExecuteUpdateEvent(statementContext);
        dbDataExecuteUpdateEvent.setStatementEnum(StatementEnum.BATCH_PREPAREEDSTATEMENT);
        applicationEventPublisher.publishEvent(dbDataExecuteUpdateEvent);
    }

    @Override
    public void onExecuteUpdate(StatementContext statementContext) {
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = new DbDataExecuteUpdateEvent(statementContext);
        dbDataExecuteUpdateEvent.setStatementEnum(StatementEnum.BATCH_PREPAREEDSTATEMENT);
        applicationEventPublisher.publishEvent(dbDataExecuteUpdateEvent);
    }

    @Override
    public void onConnectionCommit(ConnectionContext statementContext) {

    }

    @Override
    public void onTransactionCommit(TransactionContext statementContext) {

    }
}
