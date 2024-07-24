package io.microsphere.spring.db.event;

import java.util.EventListener;

public interface StatementEventListener extends EventListener {
     void  executeUpdate(StatementContext statementContext);
     void  onCommit(StatementContext statementContext);
}
