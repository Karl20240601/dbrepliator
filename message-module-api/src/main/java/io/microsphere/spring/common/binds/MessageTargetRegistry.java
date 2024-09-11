package io.microsphere.spring.common.binds;

import io.microsphere.spring.common.binds.config.ConsumerDestination;
import io.microsphere.spring.common.binds.config.ProducerDestination;
import io.microsphere.spring.common.producer.ProducerMessageChannel;
import org.springframework.messaging.SubscribableChannel;

import java.util.List;


public interface MessageTargetRegistry {
    SubscribableChannel getSubscribableChannel(String inputName);
    ConsumerDestination getConsumerDestination(String inputName);

    ProducerMessageChannel getProducerMessageChannel(String outputName);
    ProducerDestination getProducerDestination(String outputName);

    List<ConsumerDestination> getConsumerDestinations();
    List<ProducerDestination> getProducerDestinations();


}
