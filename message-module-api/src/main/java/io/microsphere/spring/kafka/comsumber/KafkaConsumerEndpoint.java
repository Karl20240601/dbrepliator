package io.microsphere.spring.kafka.comsumber;

import io.microsphere.spring.common.comsumber.AbstractConsumerEndpoint;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.BatchAcknowledgingMessageListener;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import java.util.ArrayList;
import java.util.List;



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


    @Override
    public synchronized void start() {
        this.listenerContainer.setupMessageListener(new BatchAcknowledgingMessageListener<byte[], byte[]>() {
            @Override
            public void onMessage(List<ConsumerRecord<byte[], byte[]>> data, Acknowledgment acknowledgment) {
                List<Message<byte[]>> messages = converMessageList(data);
                for (Message<byte[]> message : messages) {
                    subscribableChannel.send(message);
                }
                acknowledgment.acknowledge();
            }
        });
        this.listenerContainer.start();
       setRunning(true);
    }

    @Override
    public void stop() {
        this.listenerContainer.stop();
    }

}
