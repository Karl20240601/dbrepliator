package com.redis.annoation;

import com.redis.spring.RedisTemplateWrapperBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

public class RedisShardingSphereBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private  Environment environment;
    public static final String REDIS_SHARDING_ENABLE="redis.sharding.shpere.enable";
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry){
        if(!isPropertyExists(REDIS_SHARDING_ENABLE,"true")){
            return;
        }
        registry.registerBeanDefinition("redisShardingSphereAutoConfiguration",new RootBeanDefinition(RedisShardingSphereAutoConfiguration.class));
        registry.registerBeanDefinition(RedisTemplateWrapperBeanPostProcessor.BEAN_NAME,new RootBeanDefinition(RedisTemplateWrapperBeanPostProcessor.class));

    }


    private boolean isPropertyExists(String key, String expectValue){
        String property = environment.getProperty(key);
        return expectValue.equals(property);
    }
}
