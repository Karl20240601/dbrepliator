package io.microsphere.spring.db.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConnectionContext {
    private List<StatementContext> statementContexts;
    private final String sessionId;

    public ConnectionContext() {
        this.sessionId = UUID.randomUUID().toString();
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<StatementContext> getStatementContexts() {
        return statementContexts;
    }

    public void addStatementContexts(StatementContext statementContext) {
        if (statementContexts == null) {
            statementContexts = new ArrayList<>();
        }
        statementContexts.add(statementContext);
    }
}
