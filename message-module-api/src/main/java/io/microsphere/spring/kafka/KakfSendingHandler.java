package io.microsphere.spring.kafka;

import io.microsphere.spring.common.producer.SendingHandler;
import org.springframework.messaging.Message;

public class KakfSendingHandler implements SendingHandler {
    @Override
    public boolean sendMessage(Message<?> message, long timeout) {
        return false;
    }
}
