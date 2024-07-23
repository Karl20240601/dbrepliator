package io.microsphere.spring.db.event;

import java.util.EventListener;

public interface ConnectionEventListener extends EventListener {
     void  onCommit(ConnectionContext connectionContext);
     void  onRollback(ConnectionContext connectionContext);
}
