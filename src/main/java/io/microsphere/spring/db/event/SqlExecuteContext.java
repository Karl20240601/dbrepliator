package io.microsphere.spring.db.event;

import io.microsphere.spring.db.support.ConnectionWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 生命周期：我觉的他应该在获取连接的时候就应存在，关闭的时候结束
 */
class SqlExecuteContext {
    private Map<ConnectionWrapper, ConnectionContext> satementContextMap = new HashMap<>();

    public ConnectionContext getConnectionContext(ConnectionWrapper connection) {
        return satementContextMap.get(connection);
    }

    public void addConnectionContext(ConnectionWrapper connection, ConnectionContext connectionContext) {
        satementContextMap.put(connection, connectionContext);
    }

    public void clearConnectionContext(ConnectionWrapper connectionWrapper) {
        satementContextMap.remove(connectionWrapper);
    }

    public boolean isEmpty() {
        return satementContextMap.isEmpty();
    }
}
