package io.microsphere.spring.common.binds;

import io.microsphere.spring.common.binds.config.ConsumerDestination;
import io.microsphere.spring.common.binds.config.ProducerDestination;
import io.microsphere.spring.common.comsumber.ConsumerEndpoint;
import io.microsphere.spring.common.producer.ProducerMessageChannel;
import org.springframework.context.SmartLifecycle;

import java.util.ArrayList;
import java.util.List;

public class ReplMessageMannger implements SmartLifecycle {
    private final MessageTargetRegistry messageTargetRegistry;
    private final AbstractMessageBinder binder;
    private final List<ConsumerEndpoint> consumerEndpointList = new ArrayList<>();
    private final List<ProducerMessageChannel> producerMessageChannelList = new ArrayList<>();
    private boolean running;

    public ReplMessageMannger(MessageTargetRegistry messageTargetRegistry, AbstractMessageBinder binder) {
        this.messageTargetRegistry = messageTargetRegistry;
        this.binder = binder;
    }

    @Override
    public synchronized void start() {
        startConsumbers();
        startProducers();
        this.running = true;
    }

    private void startConsumbers() {
        List<ConsumerDestination> consumerDestinations = messageTargetRegistry.getConsumerDestinations();
        for (ConsumerDestination consumerDestination : consumerDestinations) {
            ConsumerEndpoint consumerEndpoint = binder.bindConsumber(consumerDestination.getDestinationName());
            if (consumerDestination instanceof SmartLifecycle) {
                ((SmartLifecycle) consumerDestination).start();
            }
            consumerEndpointList.add(consumerEndpoint);
        }

    }

    private void startProducers() {
        List<ProducerDestination> producerDestinations = messageTargetRegistry.getProducerDestinations();
        for (ProducerDestination producerDestination : producerDestinations) {
            ProducerMessageChannel producerMessageChannel = binder.bindProducer(producerDestination.getDestinationName());
            if (producerMessageChannel instanceof SmartLifecycle) {
                ((SmartLifecycle) producerMessageChannel).start();
            }
            producerMessageChannelList.add(producerMessageChannel);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
