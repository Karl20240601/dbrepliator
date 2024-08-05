package io.microsphere.spring.db.event;

import io.microsphere.spring.db.support.enums.StatementEnum;
import io.microsphere.spring.db.utils.SqlStringUtils;

public class DbDataExecuteUpdateEventFactory {

    public static DbDataExecuteUpdateEvent createDbDataExecuteUpdateEvent(PreparedStatementContext preparedStatementContext){
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = new DbDataExecuteUpdateEvent(preparedStatementContext);
        dbDataExecuteUpdateEvent.setStatementEnum(StatementEnum.BATCH_PREPAREEDSTATEMENT);
        dbDataExecuteUpdateEvent.setEventType(0);
        dbDataExecuteUpdateEvent.setSql(preparedStatementContext.getSql());
        dbDataExecuteUpdateEvent.setMessageKey(SqlStringUtils.findTableNameBysql(preparedStatementContext.getSql()));
        dbDataExecuteUpdateEvent.setParameters(preparedStatementContext.getSqlParameter());
        return dbDataExecuteUpdateEvent;
    }

    public static DbDataExecuteUpdateEvent createDbDataExecuteUpdateEvent(StatementContext preparedStatementContext){
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = new DbDataExecuteUpdateEvent(preparedStatementContext);
        dbDataExecuteUpdateEvent.setStatementEnum(StatementEnum.BATCH_PREPAREEDSTATEMENT);
        dbDataExecuteUpdateEvent.setEventType(0);
        dbDataExecuteUpdateEvent.setSql(preparedStatementContext.getSql());
        dbDataExecuteUpdateEvent.setMessageKey(SqlStringUtils.findTableNameBysql(preparedStatementContext.getSql()));
        return dbDataExecuteUpdateEvent;
    }
}
