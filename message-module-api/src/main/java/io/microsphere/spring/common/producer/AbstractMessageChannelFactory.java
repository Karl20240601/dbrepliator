package io.microsphere.spring.common.producer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;


public abstract class AbstractMessageChannelFactory implements MessageChannelFactory, ApplicationContextAware, EnvironmentAware, DisposableBean {
    public final static String KAFKA_CONFIG_PROPERTIES = "spring.kafka.bootstrap-servers";
    public final static String RABBITMQ_CONFIG_PROPERTIES = "spring.kafka.bootstrap-servers";
    public final static String TOPIC_CONFIG_PROPERTIES = "spring.kafka.bootstrap-servers";
    protected final static String TOPIC_NAMES_PREFIX = "db-synchronize-message-";

    protected ConfigurableApplicationContext configurableApplicationContext;
    protected Environment environment;


    private void initApplicationContext(ContextRefreshedEvent contextRefreshedEvent) {
        this.configurableApplicationContext = (ConfigurableApplicationContext) contextRefreshedEvent.getApplicationContext();
        this.environment = this.configurableApplicationContext.getEnvironment();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;

    }
}
