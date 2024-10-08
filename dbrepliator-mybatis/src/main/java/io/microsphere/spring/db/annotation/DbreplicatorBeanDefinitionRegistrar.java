package io.microsphere.spring.db.annotation;

import io.microsphere.spring.common.MessagePropertysConfiguration;
import io.microsphere.spring.common.comsumber.DefaultDispatcher;
import io.microsphere.spring.common.comsumber.MessageReplSubscribableChannel;
import io.microsphere.spring.db.config.DbReplConfiguration;
import io.microsphere.spring.db.config.MessageConfiguration;
import io.microsphere.spring.db.config.MybatisContext;
import io.microsphere.spring.db.message.consumber.messagehandler.DbReplMessageHandler;
import io.microsphere.spring.db.spring.DataSourceWrapperBeanPostProcessor;
import io.microsphere.spring.db.support.event.SqlSessionEventLlistenerImpl;
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

import static io.microsphere.spring.db.config.DbReplConfiguration.DB_REPLICATOR_DOMAIN;
import static io.microsphere.spring.db.config.MessageConfiguration.*;
import static java.util.Arrays.asList;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

public class DbreplicatorBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(DbreplicatorBeanDefinitionRegistrar.class);
    private ConfigurableEnvironment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBeanDefinition(registry, DataSourceWrapperBeanPostProcessor.BEAN_NAME, DataSourceWrapperBeanPostProcessor.class);
        registerBeanDefinition(registry, SqlSessionEventLlistenerImpl.BEAN_NAME, SqlSessionEventLlistenerImpl.class);
        registerBeanDefinition(registry, MybatisContext.BEAN_NAME, MybatisContext.class);
        registerBeanDefinition(registry, DbReplConfiguration.BEAN_NAME, DbReplConfiguration.class);
        registerBeanDefinition(registry, MessageConfiguration.BEAN_NAME, MessageConfiguration.class);

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


    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        this.environment = (ConfigurableEnvironment) environment;
    }
}
