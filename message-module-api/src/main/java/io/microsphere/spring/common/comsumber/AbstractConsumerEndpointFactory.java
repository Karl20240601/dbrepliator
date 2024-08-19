package io.microsphere.spring.common.comsumber;

import io.microsphere.spring.util.PropertySourcesUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

public abstract class AbstractConsumerEndpointFactory implements ConsumerEndpointFactory, ApplicationContextAware, EnvironmentAware {
    public static final String PROPERTY_NAME_PREFIX = "replicator.";
    public static final String PROPERTY_NAME_PREFIX_BOOTSERVER = PROPERTY_NAME_PREFIX + ".servers";
    public static final String CONSUMER_PROPERTY_NAME_PREFIX = PROPERTY_NAME_PREFIX + "consumer.";

    private volatile Map<String, Object> consumerConfigs;
    private volatile List<String> bootServers;

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

    protected Map<String, Object> getConsumerSubProperties() {
        return PropertySourcesUtils.getSubProperties(environment.getPropertySources(), environment, CONSUMER_PROPERTY_NAME_PREFIX);
    }

    protected Map<String, Object> getCommonSubProperties() {
        return PropertySourcesUtils.getSubProperties(environment.getPropertySources(), environment, CONSUMER_PROPERTY_NAME_PREFIX);
    }


}
