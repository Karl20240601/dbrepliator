package io.microsphere.spring.db.message.consumber.messagehandler;

import com.alibaba.fastjson.JSONObject;
import io.microsphere.spring.common.comsumber.ReplMessageHandler;
import io.microsphere.spring.db.config.DBReplicatorConfiguration;
import io.microsphere.spring.db.event.BatchDbDataExecuteUpdateEvent;
import io.microsphere.spring.db.event.DbDataExecuteUpdateEvent;
import io.microsphere.spring.db.serialize.api.ObjectInput;
import io.microsphere.spring.db.support.SqlParameter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;

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
        byte[] bytes = (byte[])message.getPayload();
        try {
            processDbupdateEvent(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void processBatchDbupdateEvent(byte[] bytes) throws Exception{
        DBReplicatorConfiguration bean = applicationContext.getBean(DBReplicatorConfiguration.class);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput deserialize = bean.getSerialization().deserialize(byteArrayInputStream);
        BatchDbDataExecuteUpdateEvent batchDbDataExecuteUpdateEvent = deserialize.readObject(BatchDbDataExecuteUpdateEvent.class);
    }

    private void processDbupdateEvent(byte[] bytes) throws Exception {
        DBReplicatorConfiguration bean = applicationContext.getBean(DBReplicatorConfiguration.class);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInput deserialize = bean.getSerialization().deserialize(byteArrayInputStream);
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = deserialize.readObject(DbDataExecuteUpdateEvent.class);
        DataSource dataSousrce2 = applicationContext.getBean("dataSousrce2", DataSource.class);
        PreparedStatement preparedStatement = dataSousrce2.getConnection().prepareStatement(dbDataExecuteUpdateEvent.getSql());
        SqlParameter[] parameters = dbDataExecuteUpdateEvent.getParameters();
        for (int i= 0; i<parameters.length;i++){
            preparedStatement.setObject(i+1,parameters[i].getValue());
        }
        preparedStatement.executeUpdate();
    }


}
