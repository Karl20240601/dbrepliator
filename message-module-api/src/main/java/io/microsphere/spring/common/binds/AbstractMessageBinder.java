package io.microsphere.spring.common.binds;


import io.microsphere.spring.common.binds.config.ConsumerDestination;
import io.microsphere.spring.common.binds.config.ProducerDestination;
import io.microsphere.spring.common.comsumber.ConsumerEndpoint;
import io.microsphere.spring.common.producer.ProducerMessageChannel;
import io.microsphere.spring.common.producer.SendingHandler;
import org.springframework.messaging.SubscribableChannel;


public abstract class AbstractMessageBinder implements MessageBinder {
    private final MessageTargetRegistry messageTargetRegistry;

    public AbstractMessageBinder(MessageTargetRegistry messageTargetRegistry) {
        this.messageTargetRegistry = messageTargetRegistry;
    }

    public void bindConsumber(String inputName) {
        ConsumerDestination consumerDestination = messageTargetRegistry.getConsumerDestination(inputName);
        ConsumerEndpoint consumerEndpoint = createConsumerEndpoint(consumerDestination);
        SubscribableChannel subscribableChannel = messageTargetRegistry.getSubscribableChannel(inputName);
        consumerEndpoint.setInputMessageChannel(subscribableChannel);
    }

    public void bindProducer(String outputName) {
        ProducerDestination producerDestination = messageTargetRegistry.getProducerDestination(outputName);
        SendingHandler sendingHandler = createProducerMessageHandler(producerDestination);
        ProducerMessageChannel producerMessageChannel = messageTargetRegistry.getProducerMessageChannel(outputName);
        producerMessageChannel.setSendingHandler(sendingHandler);
    }

    protected abstract ConsumerEndpoint createConsumerEndpoint(ConsumerDestination consumerDestination);

    protected abstract SendingHandler createProducerMessageHandler(ProducerDestination producerDestination);

}
