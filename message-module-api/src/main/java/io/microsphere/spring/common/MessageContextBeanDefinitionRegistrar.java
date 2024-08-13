package io.microsphere.spring.common;

import io.microsphere.spring.kafka.KafkaSubcribableChannelFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class MessageContextBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar,EnvironmentAware {
    private ConfigurableEnvironment environment;
    private static  final String SUBCRIBABLECHANNELFACTORY_BEAN_NAME="SUBCRIBABLECHANNELFACTORY" ;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBeanDefinitions(registry);
    }

    public void registerBeanDefinitions(BeanDefinitionRegistry registry) {
        //redisger 
        String property = environment.getProperty(AbstractSubcribableChannelFactory.KAFKA_CONFIG_PROPERTIES);
        if(StringUtils.hasText(property)){
            registry.registerBeanDefinition(SUBCRIBABLECHANNELFACTORY_BEAN_NAME,new RootBeanDefinition(KafkaSubcribableChannelFactory.class));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        this.environment = (ConfigurableEnvironment) environment;
    }

}
