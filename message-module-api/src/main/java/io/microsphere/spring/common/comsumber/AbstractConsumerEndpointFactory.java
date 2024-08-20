package io.microsphere.spring.common.comsumber;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractConsumerEndpointFactory implements ConsumerEndpointFactory, ApplicationContextAware, EnvironmentAware, SmartLifecycle {
    protected ConfigurableApplicationContext configurableApplicationContext;
    protected ConfigurableEnvironment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    @Override
    public void start() {
        bindConsumber();
    }

    @Override
    public void bindConsumber() {
        ConfigurableListableBeanFactory beanFactory = configurableApplicationContext.getBeanFactory();
        List<ReplMessageHandler> replCandidateMessageHandler = findReplCandidateMessageHandler(beanFactory);
        if (CollectionUtils.isEmpty(replCandidateMessageHandler)) {
            return;
        }
        String[] beanNamesForType = beanFactory.getBeanNamesForType(MessageReplSubscribableChannel.class);
        for (String beanName : beanNamesForType) {
            MessageReplSubscribableChannel subscribableChannel = configurableApplicationContext.getBean(beanName, MessageReplSubscribableChannel.class);
            boolean result = setMessageReplSubscribableChannelMessageHandler(subscribableChannel, replCandidateMessageHandler);
            if (!result) {
                continue;
            }
            doBindConsumber(subscribableChannel);
        }
    }

    public abstract void doBindConsumber(MessageReplSubscribableChannel subscribableChannel);

    private List<ReplMessageHandler> findReplCandidateMessageHandler(ConfigurableListableBeanFactory beanFactory) {
        String[] strings = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, ReplMessageHandler.class, true, false);
        if (strings == null || strings.length <= 0) {
            return null;
        }
        ArrayList<ReplMessageHandler> objects = new ArrayList<>(strings.length);
        for (String beanName : strings) {
            objects.add(beanFactory.getBean(beanName, ReplMessageHandler.class));
        }
        return objects;
    }

    private boolean setMessageReplSubscribableChannelMessageHandler(MessageReplSubscribableChannel subscribableChannel, List<ReplMessageHandler> replMessageHandlers) {
        String topic = subscribableChannel.getTopic();
        for (ReplMessageHandler replMessageHandler : replMessageHandlers) {
            if (topic.equals(replMessageHandler.getInputChannel())) {
                subscribableChannel.subscribe(replMessageHandler);
            }
        }
        return false;
    }

}
