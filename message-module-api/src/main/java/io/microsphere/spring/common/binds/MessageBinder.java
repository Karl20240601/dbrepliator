package io.microsphere.spring.common.binds;

import io.microsphere.spring.common.comsumber.ConsumerEndpoint;
import io.microsphere.spring.common.producer.ProducerMessageChannel;

/**
 * MessageBinder
 *
 */
public   interface MessageBinder {
    ConsumerEndpoint bindConsumber(String inputName);

    ProducerMessageChannel bindProducer(String outputName);

}
