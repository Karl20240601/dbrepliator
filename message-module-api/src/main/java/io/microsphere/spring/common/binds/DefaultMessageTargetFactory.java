package io.microsphere.spring.common.binds;


import io.microsphere.spring.common.binds.config.ConsumerDestination;
import io.microsphere.spring.common.binds.config.ProducerDestination;
import io.microsphere.spring.common.comsumber.DefaultDispatcher;
import io.microsphere.spring.common.comsumber.MessageDispatcher;
import io.microsphere.spring.common.comsumber.MessageReplSubscribableChannel;
import io.microsphere.spring.common.producer.ProducerMessageChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultMessageTargetFactory implements MessageTargetFactory, InitializingBean, ApplicationContextAware, MessageTargetRegistry {
    private final Map<String, SubscribableChannel> inputMap = new ConcurrentHashMap<>();
    private final Map<String, ProducerMessageChannel> outputMap = new ConcurrentHashMap<>();
    private final Map<String, ConsumerDestination> consumerDestinationMap = new ConcurrentHashMap<>();
    private final Map<String, ProducerDestination> producerDestinationMap = new ConcurrentHashMap<>();
    private final List<ConsumerDestination> consumerDestinationList = new ArrayList<>();
    private final List<ProducerDestination> producerDestination = new ArrayList<>();

    private ApplicationContext applicationContext;

    @Override
    public SubscribableChannel createInput(String inputName, String group, MessageHandler... messageHandlers) {
        if (inputMap.containsKey(inputName)) {
            return inputMap.get(inputName);
        }
        MessageDispatcher messageDispatcher = new DefaultDispatcher();
        if (messageHandlers != null && messageHandlers.length > 0) {
            messageDispatcher.addMessageHandlers(messageHandlers);
        }
        MessageReplSubscribableChannel subscribableChannel = new MessageReplSubscribableChannel(messageDispatcher, inputName, group);
        inputMap.put(inputName, subscribableChannel);
        return subscribableChannel;
    }

    @Override
    public MessageChannel createOutput(String inputName) {
        if (outputMap.containsKey(inputName)) {
            return outputMap.get(inputName);
        }
        ProducerMessageChannel producerMessageChannel = new ProducerMessageChannel(inputName);
        outputMap.put(inputName, producerMessageChannel);
        return producerMessageChannel;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        consumerDestinationList.forEach(consumerDestination -> consumerDestinationMap.put(consumerDestination.getDestinationName(), consumerDestination));
        consumerDestinationList.forEach(consumerDestination -> {
            List<String> messageHandlerBeanNames = consumerDestination.getMessageHandlerBeanNames();
            SubscribableChannel input = createInput(consumerDestination.getDestinationName(), consumerDestination.getGroupName());
            for (String messageHandlerBeanName : messageHandlerBeanNames) {
                MessageHandler messageHandler = applicationContext.getBean(messageHandlerBeanName, MessageHandler.class);
                input.subscribe(messageHandler);
            }
        });

        producerDestination.forEach(producerDestination -> producerDestinationMap.put(producerDestination.getDestinationName(), producerDestination));
        producerDestination.forEach(producerDestination -> {
            createOutput(producerDestination.getDestinationName());
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public SubscribableChannel getSubscribableChannel(String inputName) {
        return inputMap.get(inputName);
    }

    @Override
    public ConsumerDestination getConsumerDestination(String inputName) {
        return null;
    }

    @Override
    public ProducerMessageChannel getProducerMessageChannel(String outputName) {
        return outputMap.get(outputName);
    }

    @Override
    public ProducerDestination getProducerDestination(String outputName) {
        return null;
    }


}
