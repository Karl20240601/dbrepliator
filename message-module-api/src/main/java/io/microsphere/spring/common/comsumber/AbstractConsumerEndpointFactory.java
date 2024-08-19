package io.microsphere.spring.common.comsumber;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;


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
        String[] beanNamesForType = beanFactory.getBeanNamesForType(MessageReplSubscribableChannel.class);
        for (String beanName : beanNamesForType) {
            doBindConsumber(beanName);
        }
    }

    public abstract void doBindConsumber(String beanName);
}
