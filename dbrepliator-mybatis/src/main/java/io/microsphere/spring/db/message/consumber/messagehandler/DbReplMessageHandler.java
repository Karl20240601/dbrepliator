package io.microsphere.spring.db.message.consumber.messagehandler;

import com.alibaba.fastjson.JSONObject;
import io.microsphere.spring.common.comsumber.ReplMessageHandler;
import io.microsphere.spring.db.config.DBReplicatorConfiguration;
import io.microsphere.spring.db.serialize.api.ObjectInput;
import io.microsphere.spring.db.support.event.DbDataUpdateEvent;
import io.microsphere.spring.db.support.wrapper.StatementParamer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import java.io.ByteArrayInputStream;
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
            processBatchDbupdateEvent(bytes);
        } catch (Exception e) {
            throw new MessagingException(message, e);
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
        DbDataUpdateEvent dbDataUpdateEvent = deserialize.readObject(DbDataUpdateEvent.class);
        SqlSessionFactory sqlSessionFactory = applicationContext.getBean("sqlSessionFactory", SqlSessionFactory.class);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<StatementParamer> statementParamers = dbDataUpdateEvent.getSqlSessionContext().getTatementParamers();
        for (StatementParamer statementParamer : statementParamers) {
            sqlSession.update(statementParamer.getStatement(), statementParamer.getObjectParamter());
        }

    }
}
