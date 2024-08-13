package io.microsphere.spring.kafka;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.AbstractMessageChannel;
import org.springframework.messaging.support.AbstractSubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class KafkaSubscribableChannel extends AbstractMessageChannel {
    protected Log logger = LogFactory.getLog(getClass());

    private final KafkaTemplate<?, ?> template;

    protected final String topic; // NOSONAR final


    public KafkaSubscribableChannel(KafkaTemplate<?, ?> template, String topic) {
        Assert.notNull(template, "'template' cannot be null");
        Assert.notNull(topic, "'topic' cannot be null");
        this.template = template;
        this.topic = topic;
    }


    @Override
    protected boolean sendInternal(Message<?> message, long timeout) {
        try {
            this.template.send(MessageBuilder.fromMessage(message)
                    .setHeader(KafkaHeaders.TOPIC, this.topic)
                    .build())
                    .get(timeout < 0 ? Long.MAX_VALUE : timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.logger.debug("Interrupted while waiting for send result for: " + message, e);
            return false;
        } catch (ExecutionException e) {
            this.logger.error("Interrupted while waiting for send result for: " + message, e);
            return false;
        } catch (TimeoutException e) {
            this.logger.debug("Timed out while waiting for send result for: " + message, e);
            return false;
        }
        return true;
    }
}
