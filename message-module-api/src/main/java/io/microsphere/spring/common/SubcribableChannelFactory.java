package io.microsphere.spring.common;

import org.springframework.messaging.MessageChannel;

public interface SubcribableChannelFactory {
    MessageChannel createSubscribableChannel(String domains);
}
