package io.microsphere.spring.db.event;

import java.util.EventListener;

public interface DataUpdateEventListener extends EventListener {
     void  onExecuteUpdate(StatementContext statementContext);
     void  onPreparedExecuteUpdate(PreparedStatementContext statementContext);
     void  onConnectionCommit(ConnectionContext statementContext);
     void  onTransactionCommit(TransactionContext statementContext);

}
