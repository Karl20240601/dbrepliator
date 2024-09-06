package io.microsphere.spring.db.message.producer;

import io.microsphere.spring.db.config.MybatisContext;
import io.microsphere.spring.db.serialize.api.ObjectOutput;
import io.microsphere.spring.db.serialize.api.Serialization;
import io.microsphere.spring.db.support.event.DbDataUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerEventListener implements SmartApplicationListener, ApplicationContextAware, SmartInitializingSingleton {
    private static final Logger logger = LoggerFactory.getLogger(ProducerEventListener.class);

    private ApplicationContext applicationContext;
    private ExecutorService executor;
    private MybatisContext mybatisContext;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return DbDataUpdateEvent.class.equals(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        DbDataUpdateEvent dbDataExecuteUpdateEvent = (DbDataUpdateEvent) event;
        try {
            List<String> domains = mybatisContext.getDbReplConfiguration().getDomains();
            for (String domain : domains) {
                executor.execute(() -> sendRedisReplicatorMessage(domain, dbDataExecuteUpdateEvent));
            }
        } catch (Throwable e) {
            logger.warn("[Redis-Replicator-Kafka-P-F] Failed to perform Redis Replicator Kafka message sending operation.", e);
        }
    }

    private void initExecutor() {
        List<String> domains = mybatisContext.getDbReplConfiguration().getDomains();
        int size = domains.size();
        this.executor = Executors.newFixedThreadPool(size, new CustomizableThreadFactory(domains.toString()));
    }


    private void sendRedisReplicatorMessage(String domain, DbDataUpdateEvent event) {
        byte[] key = serialize(event.getMessageKey());
        byte[] value = serialize(event);
        Map<String, Object> headerTimestap = new HashMap<>();
        headerTimestap.put("messageKey", key);
        headerTimestap.put("timestamp", event.getTimestamp());
        MessageHeaders messageHeaders = new MessageHeaders(headerTimestap);
        Message<byte[]> message = MessageBuilder.createMessage(value, messageHeaders);
        mybatisContext.getMessageConfiguration().getMessageChannel(domain).send(message);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private byte[] serialize(Object o) {
        try {
            if (o == null) {
                return null;
            }
            Serialization serialization = this.mybatisContext.getSerialization();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput serialize = serialization.serialize(byteArrayOutputStream);
            serialize.writeObject(o);
            serialize.flushBuffer();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("serizal fail", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.mybatisContext = applicationContext.getBean(MybatisContext.class);
        initExecutor();
    }
}
