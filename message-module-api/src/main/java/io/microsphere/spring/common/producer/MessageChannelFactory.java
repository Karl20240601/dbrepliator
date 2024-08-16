package io.microsphere.spring.common.producer;

import org.springframework.messaging.MessageChannel;

public interface MessageChannelFactory {
    MessageChannel createMessageChannel(String domains);
}
