package io.microsphere.spring.common.comsumber;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

public interface MessageDispatcher {
    void addMessageHandler(MessageHandler messageHandler);
    void addMessageHandlers(MessageHandler... messageHandlers);

    void removeMessageHandler(MessageHandler messageHandler);

    void dipatch(Message message);
    int getCountMessageHandler();
}
