package io.microsphere.spring.common;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.InterceptableChannel;

import java.util.List;

public class AbstracConsumerPollChannel implements PollableChannel, SubscribableChannel, InterceptableChannel {
    @Override
    public Message<?> receive() {
        return null;
    }

    @Override
    public Message<?> receive(long timeout) {
        return null;
    }

    @Override
    public final boolean send(Message<?> message, long timeout) {
        throw new UnsupportedOperationException("UnsupportedOperationException");
    }

    @Override
    public boolean subscribe(MessageHandler handler) {
        return false;
    }

    @Override
    public boolean unsubscribe(MessageHandler handler) {
        return false;
    }

    @Override
    public void setInterceptors(List<ChannelInterceptor> interceptors) {

    }

    @Override
    public void addInterceptor(ChannelInterceptor interceptor) {

    }

    @Override
    public void addInterceptor(int index, ChannelInterceptor interceptor) {

    }

    @Override
    public List<ChannelInterceptor> getInterceptors() {
        return null;
    }

    @Override
    public boolean removeInterceptor(ChannelInterceptor interceptor) {
        return false;
    }

    @Override
    public ChannelInterceptor removeInterceptor(int index) {
        return null;
    }
}
