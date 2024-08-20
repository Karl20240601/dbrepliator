package io.microsphere.spring.kafka.comsumber;

import io.microsphere.spring.common.comsumber.AbstractConsumerEndpoint;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;


public class KafkaConsumerEndpoint extends AbstractConsumerEndpoint {
    private SubscribableChannel subscribableChannel;
    private final Map<String, Object> mapProperties;
    private final String[] topics;
    private final String group;
    private boolean running;
    private ConsumerFactory<byte[], byte[]> kafkaConsumerFactory;
    private ConcurrentMessageListenerContainer<byte[], byte[]> listenerContainer;
    private BeanFactory beanFactory;


    public KafkaConsumerEndpoint( BeanFactory beanFactory,Map<String, Object> mapProperties, String[] topics,String group) {
        this.beanFactory = beanFactory;
        this.mapProperties = unmodifiableMap(mapProperties);
        this.topics = topics;
        this.group = group;
        this.kafkaConsumerFactory = createReplicatorConsumerFactory();
        this.listenerContainer = createReplicatorConcurrentMessageListenerContainer();
    }

    @Override
    public void setInputMessageChannel(SubscribableChannel subscribableChannel) {
        this.subscribableChannel = subscribableChannel;
        start();
        listenerContainer.start();

    }


    private void start() {
        this.listenerContainer.setupMessageListener(new BatchAcknowledgingMessageListener<byte[], byte[]>() {
            @Override
            public void onMessage(List<ConsumerRecord<byte[], byte[]>> data, Acknowledgment acknowledgment) {
                List<Message<byte[]>> messages = converMessageList(data);
                for (Message<byte[]> message : messages) {
                    subscribableChannel.send(message);
                }
            }
        });
        this.running = true;
    }



    public ConcurrentMessageListenerContainer<byte[], byte[]> createReplicatorConcurrentMessageListenerContainer() {
        String[] topics = getTopics();
        ContainerProperties containerProperties = new ContainerProperties(topics);
        this.listenerContainer = new ConcurrentMessageListenerContainer<>(this.kafkaConsumerFactory, containerProperties);
        listenerContainer.setConcurrency(getConcurrency(topics));
        return listenerContainer;
    }

    private String[] getTopics() {
        return this.topics;
    }

    private int getConcurrency(String[] topics) {
        int topicCount = topics.length;
        //return topicCount > listenerConcurrency ? topicCount : listenerConcurrency;
        return topicCount;
    }

    private ConsumerFactory<byte[], byte[]> createReplicatorConsumerFactory() {
        DefaultKafkaConsumerFactory<byte[], byte[]> objectObjectDefaultKafkaConsumerFactory = new DefaultKafkaConsumerFactory<>(this.mapProperties);
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
}
