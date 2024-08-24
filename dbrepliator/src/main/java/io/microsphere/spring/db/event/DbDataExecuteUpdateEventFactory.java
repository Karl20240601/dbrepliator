package io.microsphere.spring.db.event;

import io.microsphere.spring.db.event.sqlmetadata.PreparedStatementSqlMetaData;
import io.microsphere.spring.db.event.sqlmetadata.StatementSqlMetaData;
import io.microsphere.spring.db.support.enums.StatementEnum;
import io.microsphere.spring.db.utils.SqlStringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class DbDataExecuteUpdateEventFactory {

    public static DbDataExecuteUpdateEvent createDbDataExecuteUpdateEvent(PreparedStatementContext preparedStatementContext) {
        PreparedStatementSqlMetaData preparedStatementSqlMetaData = preparedStatementContext.getPreparedStatementSqlMetaData();
        DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData = new DbDataExecuteUpdateEventData();

        dbDataExecuteUpdateEventData.setStatementEnum(StatementEnum.PREPAREEDSTATEMENT);
        dbDataExecuteUpdateEventData.setMessageKey(SqlStringUtils.findTableNameBysql(preparedStatementSqlMetaData.getExecuteSql()));
        dbDataExecuteUpdateEventData.setBeanName(preparedStatementContext.getDataSourceBeanName());
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = new DbDataExecuteUpdateEvent(preparedStatementContext, dbDataExecuteUpdateEventData);
        return dbDataExecuteUpdateEvent;
    }

    public static DbDataExecuteUpdateEvent createDbDataExecuteUpdateEvent(StatementContext statementContext) {
        DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData = buildStatementDbDataExecuteUpdateEventData(statementContext);
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = new DbDataExecuteUpdateEvent(statementContext, dbDataExecuteUpdateEventData);
        return dbDataExecuteUpdateEvent;
    }

    @NotNull
    private static DbDataExecuteUpdateEventData buildStatementDbDataExecuteUpdateEventData(StatementContext statementContext) {
        StatementSqlMetaData statementSqlMetaData = statementContext.getStatementSqlMetaData();
        DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData = new DbDataExecuteUpdateEventData();
        dbDataExecuteUpdateEventData.setStatementEnum(StatementEnum.STATEMENT);
        dbDataExecuteUpdateEventData.setBeanName(statementContext.getDataSourceBeanName());
        dbDataExecuteUpdateEventData.setMessageKey(SqlStringUtils.findTableNameBysql(statementSqlMetaData.getSingleExecuteSql()));
        return dbDataExecuteUpdateEventData;
    }

    @NotNull
    private static DbDataExecuteUpdateEventData buildPreparedStatementDbDataExecuteUpdateEventData(PreparedStatementContext preparedStatementContext) {
        PreparedStatementSqlMetaData preparedStatementSqlMetaData = preparedStatementContext.getPreparedStatementSqlMetaData();
        DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData = new DbDataExecuteUpdateEventData();

        dbDataExecuteUpdateEventData.setStatementEnum(StatementEnum.PREPAREEDSTATEMENT);
        dbDataExecuteUpdateEventData.setBeanName(preparedStatementContext.getDataSourceBeanName());
        dbDataExecuteUpdateEventData.setMessageKey(SqlStringUtils.findTableNameBysql(preparedStatementSqlMetaData.getExecuteSql()));
        return dbDataExecuteUpdateEventData;
    }

    public static BatchDbDataExecuteUpdateEvent createBatchDbDataExecuteUpdateEvent(ConnectionContext connectionContext) {
        List<StatementContext> statementContexts = connectionContext.getStatementContexts();
        if (CollectionUtils.isEmpty(statementContexts)) {
            throw new IllegalArgumentException("参数不能为空");
        }
        ArrayList<DbDataExecuteUpdateEventData> eventList = new ArrayList<>(statementContexts.size());
        for (StatementContext statementContext : statementContexts) {
            DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData = null;
            if (statementContext instanceof PreparedStatementContext) {
                dbDataExecuteUpdateEventData = buildPreparedStatementDbDataExecuteUpdateEventData((PreparedStatementContext) statementContext);
            } else {
                dbDataExecuteUpdateEventData = buildPreparedStatementDbDataExecuteUpdateEventData((PreparedStatementContext) statementContext);
            }
            eventList.add(dbDataExecuteUpdateEventData);
        }
        BatchDbDataExecuteUpdateEvent batchDbDataExecuteUpdateEvent = new BatchDbDataExecuteUpdateEvent(connectionContext);
        batchDbDataExecuteUpdateEvent.setUpdateEvents(eventList);
        batchDbDataExecuteUpdateEvent.setDataSourceBeanName(connectionContext.getDataSousrceBeanName());
        return batchDbDataExecuteUpdateEvent;
    }
}
