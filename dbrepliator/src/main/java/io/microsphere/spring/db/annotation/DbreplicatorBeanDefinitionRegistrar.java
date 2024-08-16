package io.microsphere.spring.db.annotation;

import io.microsphere.spring.db.event.DataUpdateEventListenerImpl;
import io.microsphere.spring.db.spring.DataSourceWrapperBeanPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import static java.util.Arrays.asList;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

public class DbreplicatorBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(DbreplicatorBeanDefinitionRegistrar.class);
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBeanDefinition(registry, DataSourceWrapperBeanPostProcessor.BEAN_NAME, DataSourceWrapperBeanPostProcessor.class);
        registerBeanDefinition(registry, DataUpdateEventListenerImpl.BEAN_NAME, DataUpdateEventListenerImpl.class);

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

}
