package io.microsphere.spring.db.event;

import java.util.EventListener;

public interface StatementEventListener extends EventListener {
     void  onExecuteUpdate(StatementContext statementContext);
     void  onConnectionCommit(ConnectionContext statementContext);
     void  onTransactionCommit(ConnectionContext statementContext);
}
