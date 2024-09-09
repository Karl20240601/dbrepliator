package io.microsphere.spring.kafka;

import io.microsphere.spring.common.binds.AbstractMessageBinder;
import io.microsphere.spring.common.binds.MessageTargetRegistry;
import io.microsphere.spring.common.binds.config.ConsumerDestination;
import io.microsphere.spring.common.binds.config.ProducerDestination;
import io.microsphere.spring.common.comsumber.ConsumerEndpoint;
import io.microsphere.spring.common.producer.SendingHandler;
import io.microsphere.spring.kafka.comsumber.KafkaConsumerEndpoint;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class KafkaMessageBinder extends AbstractMessageBinder implements ApplicationContextAware {
    protected static Log LOGGER = LogFactory.getLog(KafkaMessageBinder.class);

    private ApplicationContext applicationContext;

    public KafkaMessageBinder(MessageTargetRegistry messageTargetRegistry) {
        super(messageTargetRegistry);
    }

    @Override
    protected ConsumerEndpoint createConsumerEndpoint(ConsumerDestination consumerDestination) {
        String groupName = consumerDestination.getGroupName();
        String destinationName = consumerDestination.getDestinationName();
        Map<String, Object> configMap = consumerDestination.getConfigMap();
        ConsumerFactory<byte[], byte[]> kafkaConsumerFactory = createReplicatorConsumerFactory(configMap, groupName);
        ConcurrentMessageListenerContainer<byte[], byte[]> listenerContainer = createReplicatorConcurrentMessageListenerContainer(kafkaConsumerFactory, destinationName);
        ConsumerEndpoint consumerEndpoint = new KafkaConsumerEndpoint(kafkaConsumerFactory, listenerContainer);
        return consumerEndpoint;
    }

    @Override
    protected SendingHandler createProducerMessageHandler(ProducerDestination producerDestination) {
        SendingHandler sendingHandler = createSendingHandler(producerDestination.getConfigMap(), producerDestination.getDestinationName());
        return sendingHandler;
    }


    public ConcurrentMessageListenerContainer<byte[], byte[]> createReplicatorConcurrentMessageListenerContainer(ConsumerFactory<byte[], byte[]> kafkaConsumerFactory, String topic) {
        ContainerProperties containerProperties = new ContainerProperties(topic);
        ConcurrentMessageListenerContainer listenerContainer = new ConcurrentMessageListenerContainer<>(kafkaConsumerFactory, containerProperties);
        listenerContainer.setConcurrency(1);
        return listenerContainer;
    }


    private ConsumerFactory<byte[], byte[]> createReplicatorConsumerFactory(Map<String, Object> configMap, String group) {
        if (StringUtils.isBlank(group)) {
            configMap.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        } else {
            configMap.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        }
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        DefaultKafkaConsumerFactory<byte[], byte[]> objectObjectDefaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(configMap);
        objectObjectDefaultKafkaConsumerFactory.setKeyDeserializer(new ByteArrayDeserializer());
        objectObjectDefaultKafkaConsumerFactory.setValueDeserializer(new ByteArrayDeserializer());
        return objectObjectDefaultKafkaConsumerFactory;
    }


    private List<Message<byte[]>> converMessageList(List<ConsumerRecord<byte[], byte[]>> data) {
        List<Message<byte[]>> dataList = new ArrayList<>(data.size());
        for (ConsumerRecord<byte[], byte[]> consumerRecord : data) {
            byte[] value = consumerRecord.value();
            byte[] key = consumerRecord.key();
            GenericMessage<byte[]> genericMessage = new GenericMessage<byte[]>(value);
            dataList.add(genericMessage);
        }
        return dataList;
    }


    private ProducerFactory<byte[], byte[]> createReplicatorProducerFactory(Map<String, Object> configMap) {
        DefaultKafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory<>(configMap);
        producerFactory.setKeySerializer(new ByteArraySerializer());
        producerFactory.setValueSerializer(new ByteArraySerializer());
        return producerFactory;
    }

    private SendingHandler createSendingHandler(Map<String, Object> configMap, String topic) {
        KafkaTemplate kafkaTemplate = null;

        try {
            kafkaTemplate = this.applicationContext.getBean(KafkaTemplate.class);
        } catch (Exception e) {
            LOGGER.error("applicationContext can not find kafkaTemplate");
        }
        if (kafkaTemplate == null) {
            ProducerFactory<byte[], byte[]> replicatorProducerFactory = createReplicatorProducerFactory(configMap);
            kafkaTemplate = new KafkaTemplate<>(replicatorProducerFactory);
        }
        return new KafkaSendingHandler(kafkaTemplate, topic);
    }

    static class KafkaSendingHandler implements SendingHandler {

        private final KafkaTemplate kafkaTemplate;
        private final String topic;

        public KafkaSendingHandler(KafkaTemplate kafkaTemplate, String topic) {
            this.kafkaTemplate = kafkaTemplate;
            this.topic = topic;
        }

        @Override
        public boolean sendMessage(Message<?> message, long timeout) {
            try {
                this.kafkaTemplate.send(MessageBuilder.fromMessage(message)
                        .setHeader(KafkaHeaders.TOPIC, topic)
                        .build())
                        .get(timeout < 0 ? Long.MAX_VALUE : timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LOGGER.debug("Interrupted while waiting for send result for: " + message, e);
                return false;
            } catch (ExecutionException e) {
                LOGGER.error("Interrupted while waiting for send result for: " + message, e);
                return false;
            } catch (TimeoutException e) {
                LOGGER.debug("Timed out while waiting for send result for: " + message, e);
                return false;
            }
            return true;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
