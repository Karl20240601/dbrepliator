package io.microsphere.spring.kafka.comsumber;

import io.microsphere.spring.common.comsumber.AbstractConsumerEndpointFactory;
import io.microsphere.spring.common.comsumber.AbstractSubscribableChannel;
import io.microsphere.spring.common.comsumber.ConsumerEndpoint;
import io.microsphere.spring.common.comsumber.DefaultDispatcher;
import org.springframework.messaging.SubscribableChannel;

import java.util.HashMap;
import java.util.Map;

public class KafkaConsumerEndpointFactory extends AbstractConsumerEndpointFactory {

    public static final String KAFKA_CONSUMER_PROPERTY_NAME_PREFIX =  + "consumer.";


    private final Map<String, SubscribableChannel> topicSubscribableChannel = new HashMap<>();

    @Override
    public ConsumerEndpoint createConsumerEndpoint(String topic, String group) {
        DefaultDispatcher defaultDispatcher = new DefaultDispatcher();
        AbstractSubscribableChannel abstractSubscribableChannel = new AbstractSubscribableChannel(defaultDispatcher, topic, group);
        String[] topics = {topic};
        KafkaConsumerEndpoint kafkaConsumerEndpoint = new KafkaConsumerEndpoint(abstractSubscribableChannel, new HashMap<>(), topics, group);
        return kafkaConsumerEndpoint;
    }
}
