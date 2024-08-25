package io.microsphere.spring.db.message.consumber.messagehandler;

import com.alibaba.fastjson.JSONObject;
import io.microsphere.spring.common.comsumber.ReplMessageHandler;
import io.microsphere.spring.db.config.DBReplicatorConfiguration;
import io.microsphere.spring.db.event.BatchDbDataExecuteUpdateEvent;
import io.microsphere.spring.db.event.DbDataExecuteUpdateEventData;
import io.microsphere.spring.db.event.sqlmetadata.PreparedStatementSqlMetaData;
import io.microsphere.spring.db.event.sqlmetadata.StatementSqlMetaData;
import io.microsphere.spring.db.serialize.api.ObjectInput;
import io.microsphere.spring.db.support.SqlParameter;
import io.microsphere.spring.db.support.enums.StatementEnum;
import io.microsphere.spring.db.utils.StreamUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.util.List;

public class DbReplMessageHandler implements ReplMessageHandler, ApplicationContextAware {
    private final String inputChannel;
    private ApplicationContext applicationContext;

    public DbReplMessageHandler(String inputChannel) {
        this.inputChannel = inputChannel;
    }

    @Override
    public String getInputChannel() {
        return inputChannel;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        Boolean isBatchEvent = message.getHeaders().get("isBatchEvent", boolean.class);
        System.out.println(isBatchEvent);
        System.out.println(JSONObject.toJSONString(message));
        byte[] bytes = (byte[]) message.getPayload();
        try {
            if (isBatchEvent) {
                processBatchDbupdateEvent(bytes);
            } else {
                processDbupdateEvent(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void processBatchDbupdateEvent(byte[] bytes) throws Exception {
        DBReplicatorConfiguration bean = applicationContext.getBean(DBReplicatorConfiguration.class);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput deserialize = bean.getSerialization().deserialize(byteArrayInputStream);
        DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData = deserialize.readObject(DbDataExecuteUpdateEventData.class);
        DataSource dataSousrce2 = applicationContext.getBean("dataSousrce2", DataSource.class);
        Connection connection = dataSousrce2.getConnection();
        if (StatementEnum.STATEMENT == dbDataExecuteUpdateEventData.getStatementEnum()) {
            processtatement(connection, dbDataExecuteUpdateEventData);
        } else {
            processPrepareStatement(connection, dbDataExecuteUpdateEventData);
        }
        connection.close();
    }

    private void processDbupdateEvent(byte[] bytes) throws Exception {
        DBReplicatorConfiguration bean = applicationContext.getBean(DBReplicatorConfiguration.class);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput deserialize = bean.getSerialization().deserialize(byteArrayInputStream);
        List<DbDataExecuteUpdateEventData> dbDataExecuteUpdateEvent = (List<DbDataExecuteUpdateEventData>) deserialize.readObject(List.class);
        DataSource dataSousrce2 = applicationContext.getBean("dataSousrce2", DataSource.class);
        Connection connection = dataSousrce2.getConnection();
        for (int i = 0; i < dbDataExecuteUpdateEvent.size(); i++) {
            DbDataExecuteUpdateEventData dbDataExecuteUpdateEventData = dbDataExecuteUpdateEvent.get(i);
            if (StatementEnum.STATEMENT == dbDataExecuteUpdateEventData.getStatementEnum()) {
                processtatement(connection, dbDataExecuteUpdateEventData);
            } else {
                processPrepareStatement(connection, dbDataExecuteUpdateEventData);
            }
        }
        connection.close();
    }


    private void processtatement(Connection connection, DbDataExecuteUpdateEventData dbDataExecuteUpdateEvent) throws SQLException {
        StatementSqlMetaData sqlMetaData = (StatementSqlMetaData) dbDataExecuteUpdateEvent.getSqlMetaData();
        Statement statement = connection.createStatement();
        boolean batchUpdate = dbDataExecuteUpdateEvent.isBatchUpdate();
        if (batchUpdate) {
            List<String> batchleExecuteSql = sqlMetaData.takeBatchleExecuteSql();
            for (String sql : batchleExecuteSql) {
                statement.addBatch(sql);
            }
            statement.executeBatch();
            return;
        }
        statement.execute(sqlMetaData.takeSingleExecuteSql());
    }


    private void processPrepareStatement(Connection connection, DbDataExecuteUpdateEventData dbDataExecuteUpdateEvent) throws SQLException, UnsupportedEncodingException {
        PreparedStatementSqlMetaData sqlMetaData = (PreparedStatementSqlMetaData) dbDataExecuteUpdateEvent.getSqlMetaData();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlMetaData.getExecuteSql());
        boolean batchUpdate = dbDataExecuteUpdateEvent.isBatchUpdate();
        List<SqlParameter[]> batchArgs = sqlMetaData.getBatchArgs();
        for (SqlParameter[] parameterArray : batchArgs) {
            setParameter(preparedStatement, parameterArray);
            preparedStatement.addBatch();
        }
        preparedStatement.executeUpdate();
    }

    private void setParameter(PreparedStatement preparedStatement, SqlParameter[] parameterArray) throws SQLException, UnsupportedEncodingException {
        for (int i = 0; i < parameterArray.length; i++) {
            SqlParameter sqlParameter = parameterArray[i];
            if (sqlParameter.getSqlType() == JDBCType.BLOB) {
                preparedStatement.setBinaryStream(i + 1, new ByteArrayInputStream((byte[]) sqlParameter.getValue()));
            }
            if (sqlParameter.getSqlType() == JDBCType.CLOB) {
                InputStreamReader inputStreamReader = new InputStreamReader(new ByteArrayInputStream((byte[]) sqlParameter.getValue()), StreamUtils.DEFAULT_CHARSET);
                preparedStatement.setClob(i + 1, inputStreamReader);
            }
            preparedStatement.setObject(i + 1, sqlParameter.getValue(), sqlParameter.getSqlType());
        }

    }



}
