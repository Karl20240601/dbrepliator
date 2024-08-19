package io.microsphere.spring.common.comsumber;

import io.microsphere.spring.common.comsumber.MessageDispatcher;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.AbstractMessageChannel;

import java.util.ArrayList;
import java.util.List;

public class AbstractSubscribableChannel extends AbstractMessageChannel implements SubscribableChannel {
    private final MessageDispatcher messageDispatcher;
    private final String topic;
    private final String group;

    public AbstractSubscribableChannel(MessageDispatcher messageDispatcher, String topic, String group) {
        this.messageDispatcher = messageDispatcher;
        this.topic = topic;
        this.group = group;
    }

    @Override
    protected boolean sendInternal(Message<?> message, long timeout) {
        this.messageDispatcher.dipatch(message);
        return true;
    }

    @Override
    public boolean subscribe(MessageHandler handler) {
        this.messageDispatcher.addMessageHandler(handler);
        return true;
    }

    @Override
    public boolean unsubscribe(MessageHandler handler) {
        this.messageDispatcher.removeMessageHandler(handler);
        return true;
    }
}
