package io.microsphere.spring.db.kafka.producer;

import com.alibaba.fastjson.JSONObject;
import io.microsphere.spring.db.config.DBReplicatorConfiguration;
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
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

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
        String topic = createTopic(domain);
        // Almost all RedisCommands interface methods take the first argument as Key
        byte[] key = serialize(event.getMessageKey());
        byte[] value = serialize(event);
        // Use a timestamp of the event
        long timestamp = event.getTimestamp();
        ListenableFuture<SendResult<byte[], byte[]>> future = kafkaTemplate.send(topic, null, timestamp, key, value);
        future.addCallback(new ListenableFutureCallback<SendResult<byte[], byte[]>>() {

            @Override
            public void onSuccess(SendResult<byte[], byte[]> result) {
                logger.debug("[Redis-Replicator-Kafka-P-S] Kafka message sending operation succeeds. Topic: {}, key: {}, data size: {} bytes, event: {}",
                        "topics", key, value.length, event);
            }

            @Override
            public void onFailure(Throwable e) {
                logger.warn("[Redis-Replicator-Kafka-P-F] Kafka message sending operation failed. Topic: {}, key: {}, data size: {} bytes",
                        "topics", key, value.length, e);
            }

        });
    }



    private String createTopic(String domain) {
        return dbReplicatorConfiguration.keyPrefix() + domain;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.dbReplicatorConfiguration = applicationContext.getBean(DBReplicatorConfiguration.class);
    }

    private byte[] serialize(Object o){
        try {
            Serialization serialization = dbReplicatorConfiguration.getSerialization();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput serialize = serialization.serialize(byteArrayOutputStream);
            serialize.writeObject(o);
            serialize.flushBuffer();
            return byteArrayOutputStream.toByteArray();
        }catch (IOException e){
            logger.error("serizal fail");
            throw new RuntimeException(e);
        }
    }
}
