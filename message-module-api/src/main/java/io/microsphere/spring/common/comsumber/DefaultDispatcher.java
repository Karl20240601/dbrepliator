package io.microsphere.spring.common.comsumber;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

import java.util.HashSet;
import java.util.Set;

public class DefaultDispatcher implements MessageDispatcher {
    private final Set<MessageHandler> messageHandlers = new HashSet<>();

    @Override
    public void addMessageHandler(MessageHandler messageHandler) {
        messageHandlers.add(messageHandler);

    }

    @Override
    public void removeMessageHandler(MessageHandler messageHandler) {
        messageHandlers.remove(messageHandler);
    }

    @Override
    public void dipatch(Message message) {
        for (MessageHandler messageHandler : messageHandlers) {
            messageHandler.handleMessage(message);
        }
    }

    @Override
    public int getCountMessageHandler() {
        return messageHandlers.size();
    }
}
