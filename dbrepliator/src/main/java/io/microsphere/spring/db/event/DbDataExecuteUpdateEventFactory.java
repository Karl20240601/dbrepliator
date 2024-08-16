package io.microsphere.spring.db.event;

import io.microsphere.spring.db.support.enums.StatementEnum;
import io.microsphere.spring.db.utils.SqlStringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class DbDataExecuteUpdateEventFactory {

    public static DbDataExecuteUpdateEvent createDbDataExecuteUpdateEvent(PreparedStatementContext preparedStatementContext) {
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = new DbDataExecuteUpdateEvent(preparedStatementContext);
        dbDataExecuteUpdateEvent.setStatementEnum(StatementEnum.PREPAREEDSTATEMENT);
        dbDataExecuteUpdateEvent.setEventType(0);
        dbDataExecuteUpdateEvent.setSql(preparedStatementContext.getSql());
        dbDataExecuteUpdateEvent.setMessageKey(SqlStringUtils.findTableNameBysql(preparedStatementContext.getSql()));
        dbDataExecuteUpdateEvent.setParameters(preparedStatementContext.getSqlParameter());
        return dbDataExecuteUpdateEvent;
    }

    public static DbDataExecuteUpdateEvent createDbDataExecuteUpdateEvent(StatementContext preparedStatementContext) {
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = new DbDataExecuteUpdateEvent(preparedStatementContext);
        dbDataExecuteUpdateEvent.setStatementEnum(StatementEnum.STATEMENT);
        dbDataExecuteUpdateEvent.setEventType(0);
        dbDataExecuteUpdateEvent.setSql(preparedStatementContext.getSql());
        dbDataExecuteUpdateEvent.setMessageKey(SqlStringUtils.findTableNameBysql(preparedStatementContext.getSql()));
        return dbDataExecuteUpdateEvent;
    }

    public static BatchDbDataExecuteUpdateEvent createBatchDbDataExecuteUpdateEvent(ConnectionContext connectionContext) {
        List<StatementContext> statementContexts = connectionContext.getStatementContexts();
        if (CollectionUtils.isEmpty(statementContexts)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        ArrayList<DbDataExecuteUpdateEvent> eventList = new ArrayList<>(statementContexts.size());
        for (StatementContext statementContext : statementContexts) {
            DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = createDbDataExecuteUpdateEvent(statementContext);
            if (statementContext instanceof PreparedStatementContext) {
                dbDataExecuteUpdateEvent.setStatementEnum(StatementEnum.PREPAREEDSTATEMENT);
            } else {
                dbDataExecuteUpdateEvent.setStatementEnum(StatementEnum.STATEMENT);
            }
            eventList.add(dbDataExecuteUpdateEvent);
        }

        BatchDbDataExecuteUpdateEvent batchDbDataExecuteUpdateEvent = new BatchDbDataExecuteUpdateEvent(connectionContext);
        batchDbDataExecuteUpdateEvent.setUpdateEvents(eventList);
        return batchDbDataExecuteUpdateEvent;
    }
}
