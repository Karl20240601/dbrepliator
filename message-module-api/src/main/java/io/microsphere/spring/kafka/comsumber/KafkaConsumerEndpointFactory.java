package io.microsphere.spring.kafka.comsumber;

import io.microsphere.spring.common.comsumber.AbstractConsumerEndpointFactory;
import io.microsphere.spring.common.comsumber.ConsumerEndpoint;
import io.microsphere.spring.common.comsumber.MessageReplSubscribableChannel;
import org.springframework.util.StringUtils;

import java.util.HashMap;

public class KafkaConsumerEndpointFactory extends AbstractConsumerEndpointFactory {
    @Override
    public ConsumerEndpoint createConsumerEndpoint(String topic, String group) {
        String[] topics = StringUtils.split(topic,",");
        KafkaConsumerEndpoint kafkaConsumerEndpoint = new KafkaConsumerEndpoint(configurableApplicationContext.getBeanFactory(),new HashMap<>(), topics, group);
        return kafkaConsumerEndpoint;
    }

    @Override
    public void doBindConsumber(String tpoics) {
        MessageReplSubscribableChannel subscribableChannel = super.configurableApplicationContext.getBean(tpoics, MessageReplSubscribableChannel.class);
        String topic = subscribableChannel.getTopic();
        String group = subscribableChannel.getGroup();
        ConsumerEndpoint consumerEndpoint = createConsumerEndpoint(topic, group);
        consumerEndpoint.setInputMessageChannel(subscribableChannel);

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
