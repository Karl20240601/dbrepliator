package io.microsphere.spring.db.event;

import java.util.EventListener;

public interface PreparedStatementEventListener extends StatementEventListener {
     void  onPreparedExecuteUpdate(PreparedStatementContext statementContext);
}
