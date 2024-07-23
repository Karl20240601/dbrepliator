package io.microsphere.spring.db.event;

import java.util.EventListener;

public interface StatementEventListener extends EventListener {
     void  onexecuteBatch(StatementContext statementContext);
     void  onRollback(StatementContext statementContext);
     void  executeUpdate(StatementContext statementContext);
     void  onCommit(StatementContext statementContext);
}
