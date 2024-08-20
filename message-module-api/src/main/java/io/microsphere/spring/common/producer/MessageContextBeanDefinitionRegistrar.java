package io.microsphere.spring.common.producer;

import io.microsphere.spring.kafka.KafkaMessageChannelFactory;
import io.microsphere.spring.kafka.comsumber.KafkaConsumerEndpointFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import io.microsphere.spring.common.MessagePropertysConfiguration;

public class MessageContextBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private ConfigurableEnvironment environment;
    private static final String SUBCRIBABLECHANNELFACTORY_BEAN_NAME = "subcribablechannelfactory";
    private static final String KAFKACONSUMERENDPOINTFACTORY_BEAN_NAME = "kafkaConsumerEndpointFactory";

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBeanDefinitions(registry);
    }

    public void registerBeanDefinitions(BeanDefinitionRegistry registry) {
        Boolean producerEnable = environment.getProperty(MessagePropertysConfiguration.PRODUCER_PROPERTY_ENABLE, boolean.class, false);
        if (producerEnable) {
            //redisger
            String kafkaServerList = environment.getProperty(AbstractMessageChannelFactory.KAFKA_CONFIG_PROPERTIES);
            if (StringUtils.hasText(kafkaServerList)) {
                registry.registerBeanDefinition(SUBCRIBABLECHANNELFACTORY_BEAN_NAME, new RootBeanDefinition(KafkaMessageChannelFactory.class));
            }
        }

        Boolean comsumberEnable = environment.getProperty(MessagePropertysConfiguration.CONSUMER_PROPERTY_ENABLE, boolean.class, false);
        if (comsumberEnable) {
            //redisger
            String kafkaServerList = environment.getProperty(AbstractMessageChannelFactory.KAFKA_CONFIG_PROPERTIES);
            if (StringUtils.hasText(kafkaServerList)) {
                registry.registerBeanDefinition(KAFKACONSUMERENDPOINTFACTORY_BEAN_NAME, new RootBeanDefinition(KafkaConsumerEndpointFactory.class));
            }
        }

    }

    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        this.environment = (ConfigurableEnvironment) environment;
    }

}
