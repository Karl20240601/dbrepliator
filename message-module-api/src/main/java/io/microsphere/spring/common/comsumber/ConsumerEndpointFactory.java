package io.microsphere.spring.common.comsumber;

import org.springframework.messaging.SubscribableChannel;

public interface ConsumerEndpointFactory {
    ConsumerEndpoint  createConsumerEndpoint(String tpoics,String group);
    void   bindConsumber();
}
