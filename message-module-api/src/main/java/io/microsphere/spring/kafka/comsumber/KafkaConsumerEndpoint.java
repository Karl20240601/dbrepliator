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
    private final ConsumerFactory<byte[], byte[]> kafkaConsumerFactory;
    private final ConcurrentMessageListenerContainer<byte[], byte[]> listenerContainer;


    public KafkaConsumerEndpoint(ConsumerFactory<byte[], byte[]> kafkaConsumerFactory,ConcurrentMessageListenerContainer<byte[], byte[]> listenerContainer) {
        this.kafkaConsumerFactory = kafkaConsumerFactory;
        this.listenerContainer = listenerContainer;
    }

    @Override
    public void setInputMessageChannel(SubscribableChannel subscribableChannel) {
        this.subscribableChannel = subscribableChannel;
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
