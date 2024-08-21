package io.microsphere.spring.db.message.producer;

import io.microsphere.spring.db.config.DBReplicatorConfiguration;
import io.microsphere.spring.db.config.MessageConfiguration;
import io.microsphere.spring.db.event.DbDataExecuteUpdateEvent;
import io.microsphere.spring.db.serialize.api.ObjectOutput;
import io.microsphere.spring.db.serialize.api.Serialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaProducerEventListener implements SmartApplicationListener, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerEventListener.class);

    private KafkaTemplate<byte[], byte[]> kafkaTemplate;
    private ApplicationContext applicationContext;
    private ExecutorService executor;
    private DBReplicatorConfiguration dbReplicatorConfiguration;
    private MessageConfiguration messageConfiguration;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return DbDataExecuteUpdateEvent.class.equals(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        DbDataExecuteUpdateEvent dbDataExecuteUpdateEvent = (DbDataExecuteUpdateEvent) event;
        try {
            String beanName = dbDataExecuteUpdateEvent.getBeanName();
            List<String> domains = dbReplicatorConfiguration.getDomains(beanName);
            for (String domain : domains) {
                executor.execute(() -> sendRedisReplicatorKafkaMessage(domain, dbDataExecuteUpdateEvent));
            }
        } catch (Throwable e) {
            logger.warn("[Redis-Replicator-Kafka-P-F] Failed to perform Redis Replicator Kafka message sending operation.", e);
        }
    }

    private void initExecutor() {
        List<String> domains = dbReplicatorConfiguration.getDomains();
        int size = domains.size();
        this.executor = Executors.newFixedThreadPool(size, new CustomizableThreadFactory(domains.toString()));
    }



    private void sendRedisReplicatorKafkaMessage(String domain, DbDataExecuteUpdateEvent event) {
        byte[] key = serialize(event.getMessageKey());
        byte[] value = serialize(event);
        Message<byte[]> message = MessageBuilder
                .withPayload(value)
                .setHeader("messageKey", key)
                .setHeader("timestamp", event.getTimestamp())
                .build();
        messageConfiguration.getMessageChannel(domain).send(message);
    }


    private String createTopic(String domain) {
        return dbReplicatorConfiguration.keyPrefix() + domain;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.dbReplicatorConfiguration = applicationContext.getBean(DBReplicatorConfiguration.class);
        this.messageConfiguration = applicationContext.getBean(MessageConfiguration.class);
    }

    private byte[] serialize(Object o) {
        try {
            Serialization serialization = dbReplicatorConfiguration.getSerialization();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput serialize = serialization.serialize(byteArrayOutputStream);
            serialize.writeObject(o);
            serialize.flushBuffer();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("serizal fail");
            throw new RuntimeException(e);
        }
    }
}
