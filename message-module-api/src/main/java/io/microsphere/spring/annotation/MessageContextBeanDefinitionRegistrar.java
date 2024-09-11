package io.microsphere.spring.annotation;

import io.microsphere.spring.common.binds.DefaultMessageTargetFactory;
import io.microsphere.spring.common.binds.ReplMessageMannger;
import io.microsphere.spring.kafka.KafkaMessageBinder;
import io.microsphere.spring.kafka.config.KafkaConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static io.microsphere.spring.common.MessagePropertysConfiguration.KAFKA_CONFIG_PROPERTIES;
import static java.util.Arrays.asList;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

public class MessageContextBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final Logger logger = LoggerFactory.getLogger(MessageContextBeanDefinitionRegistrar.class);
    private ConfigurableEnvironment environment;
    private static final String KAFKAMESSAGEBINDER_BEAN_NAME = "KAFKAMESSAGEBINDER";
    private static final String CONFIGBUILDER_BEAN_NAME = "configBuilder";
    private static final String DEFAULTMESSAGETARGETFACTORY = "defaultMessageTargetFactory";
    private static final String DBREPLMESSAGEBINDER = "dbReplMessageBinder";
    private static final String REPLMESSAGEMANNGER = "replMessageMannger";


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerBeanDefinitions(registry);
    }

    public void registerBeanDefinitions(BeanDefinitionRegistry registry) {
        String kafkaServerList = environment.getProperty(KAFKA_CONFIG_PROPERTIES);
        if (StringUtils.hasText(kafkaServerList)) {
            registry.registerBeanDefinition(KAFKAMESSAGEBINDER_BEAN_NAME, new RootBeanDefinition(KafkaMessageBinder.class));
            registerBeanDefinition(registry, CONFIGBUILDER_BEAN_NAME, KafkaConfigBuilder.class, environment);
            registerBeanDefinition(registry, DEFAULTMESSAGETARGETFACTORY, DefaultMessageTargetFactory.class);
            registerBeanDefinition(registry, DBREPLMESSAGEBINDER, KafkaMessageBinder.class,DEFAULTMESSAGETARGETFACTORY );
            registerBeanDefinition(registry, REPLMESSAGEMANNGER, ReplMessageMannger.class, DEFAULTMESSAGETARGETFACTORY, DBREPLMESSAGEBINDER);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, environment);
        this.environment = (ConfigurableEnvironment) environment;
    }


    private void registerBeanDefinition(BeanDefinitionRegistry registry, String beanName, Class<?>
            beanClass, Object... constructorArgs) {
        if (!registry.containsBeanDefinition(beanName)) {
            BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(beanClass);
            for (Object constructorArg : constructorArgs) {
                beanDefinitionBuilder.addConstructorArgValue(constructorArg);
            }
            registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
            logger.debug("registerBeanDefinition Component[name : '{}' , class : {} , args : {}] registered", beanName, beanClass, asList(constructorArgs));
        }
    }

    private void registerBeanDefinition(BeanDefinitionRegistry registry, String beanName, Class<?>
            beanClass, String... dependOns) {
        if (!registry.containsBeanDefinition(beanName)) {
            BeanDefinitionBuilder beanDefinitionBuilder = genericBeanDefinition(beanClass);
            for (String dependOn : dependOns) {
                beanDefinitionBuilder.addDependsOn(dependOn);
            }
            registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
            logger.debug("registerBeanDefinition Component[name : '{}' , class : {} , args : {}] registered", beanName, beanClass, asList(dependOns));
        }
    }

}
