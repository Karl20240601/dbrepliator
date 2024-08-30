package io.microsphere.spring.db.annotation;

import io.microsphere.spring.common.comsumber.DefaultDispatcher;
import io.microsphere.spring.common.comsumber.MessageReplSubscribableChannel;
import io.microsphere.spring.db.event.DataUpdateEventListenerImpl;
import io.microsphere.spring.db.message.consumber.messagehandler.DbReplMessageHandler;
import io.microsphere.spring.db.spring.DataSourceWrapperBeanPostProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import static io.microsphere.spring.db.config.DBReplicatorConfiguration.*;
import static java.util.Arrays.asList;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

public class DbreplicatorBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(DbreplicatorBeanDefinitionRegistrar.class);
    private ConfigurableEnvironment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBeanDefinition(registry, DataSourceWrapperBeanPostProcessor.BEAN_NAME, DataSourceWrapperBeanPostProcessor.class);
        registerBeanDefinition(registry, DataUpdateEventListenerImpl.BEAN_NAME, DataUpdateEventListenerImpl.class);
        registerMessageHandlerBeanDefinition(registry);

    }

    private void registerBeanDefinition(BeanDefinitionRegistry registry, String beanName, Class<?>
            beanClass, Object... constructorArgs) {
        if (!registry.containsBeanDefinition(beanName)) {
            BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(beanClass);
            for (Object constructorArg : constructorArgs) {
                beanDefinitionBuilder.addConstructorArgValue(constructorArg);
            }
            registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
            logger.debug("Redis Interceptor Component[name : '{}' , class : {} , args : {}] registered", beanName, beanClass, asList(constructorArgs));
        }
    }

    private void registerMessageHandlerBeanDefinition(BeanDefinitionRegistry registry) {
        String property = environment.getProperty(DB_REPLICATOR_DOMAINS);
        if (StringUtils.isBlank(property)) {
            logger.debug("property '{}' not config",DB_REPLICATOR_DOMAINS);
            return;
        }
        String[] split = StringUtils.split(property, ",");
        for (String beanNamePrefix : split) {
            registerMessageHandler(registry, beanNamePrefix);
            registerSubcriberChannel(registry, beanNamePrefix);
        }
    }

    private void registerMessageHandler(BeanDefinitionRegistry registry, String beanNamePrefix) {
        String beanName = beanNamePrefix + MESSAGEHANDLER_NAME_PREFIX;
        BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(DbReplMessageHandler.class);
        beanDefinitionBuilder.addConstructorArgValue(getTopicPrefix(DB_REPLICATOR_TOPICPREFIX) + beanNamePrefix);
        registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    private void registerSubcriberChannel(BeanDefinitionRegistry registry, String beanNamePrefix) {
        String beanName2 = beanNamePrefix + MESSAGEREPLSUBSCRIBABLECHANNEL;
        BeanDefinitionBuilder beanDefinitionBuilder2 = genericBeanDefinition(MessageReplSubscribableChannel.class);
        beanDefinitionBuilder2.addConstructorArgValue(new DefaultDispatcher());
        beanDefinitionBuilder2.addConstructorArgValue(getTopicPrefix(DB_REPLICATOR_TOPICPREFIX) + beanNamePrefix);
        beanDefinitionBuilder2.addConstructorArgValue(beanNamePrefix + DB_REPLICATOR_TOPICPREFIX_DEFAULT+"group");
        registry.registerBeanDefinition(beanName2, beanDefinitionBuilder2.getBeanDefinition());
    }

    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        this.environment = (ConfigurableEnvironment) environment;
    }

    private String getTopicPrefix(String property){
        return environment.getProperty(property,DB_REPLICATOR_TOPICPREFIX_DEFAULT);
    }
}
