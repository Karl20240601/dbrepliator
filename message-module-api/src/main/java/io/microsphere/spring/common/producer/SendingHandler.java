package io.microsphere.spring.common.producer;

import org.springframework.messaging.Message;

public interface SendingHandler {
    boolean sendMessage(Message<?> message, long timeout);
}
