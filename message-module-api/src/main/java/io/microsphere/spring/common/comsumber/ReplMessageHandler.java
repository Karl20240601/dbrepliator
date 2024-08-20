package io.microsphere.spring.common.comsumber;

import org.springframework.messaging.MessageHandler;

public interface ReplMessageHandler extends MessageHandler {
    String getInputChannel();
}
