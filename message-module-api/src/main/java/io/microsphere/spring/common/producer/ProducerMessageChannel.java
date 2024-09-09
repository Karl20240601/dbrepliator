package io.microsphere.spring.common.producer;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.AbstractMessageChannel;

public class ProducerMessageChannel extends AbstractMessageChannel {
    private final  String topic;

    public ProducerMessageChannel(String topic) {
        this.topic = topic;
    }

    private SendingHandler sendingHandler;

    public String getTopic() {
        return topic;
    }

    public SendingHandler getSendingHandler() {
        return sendingHandler;
    }

    public void setSendingHandler(SendingHandler sendingHandler) {
        this.sendingHandler = sendingHandler;
    }

    @Override
    protected boolean sendInternal(Message<?> message, long timeout) {
        return sendingHandler.sendMessage(message,timeout);
    }

}
