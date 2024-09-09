package io.microsphere.spring.annotation;

import io.microsphere.spring.kafka.KafkaMessageBinder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static io.microsphere.spring.common.MessagePropertysConfiguration.KAFKA_CONFIG_PROPERTIES;

public class MessageContextBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private ConfigurableEnvironment environment;
    private static final String KAFKAMESSAGEBINDER_BEAN_NAME = "KAFKAMESSAGEBINDER";


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBeanDefinitions(registry);
    }

    public void registerBeanDefinitions(BeanDefinitionRegistry registry) {
        String kafkaServerList = environment.getProperty(KAFKA_CONFIG_PROPERTIES);
        if (StringUtils.hasText(kafkaServerList)) {
            registry.registerBeanDefinition(KAFKAMESSAGEBINDER_BEAN_NAME, new RootBeanDefinition(KafkaMessageBinder.class));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        this.environment = (ConfigurableEnvironment) environment;
    }

}
