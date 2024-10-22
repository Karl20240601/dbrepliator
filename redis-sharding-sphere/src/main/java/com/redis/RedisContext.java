package com.redis;

import com.redis.interceptor.Interceptor;
import com.redis.registy.KeyRegistry;
import com.redis.registy.RedisConnectionRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RedisContext implements EnvironmentAware, ApplicationContextAware, InitializingBean, BeanClassLoaderAware {
    private ConfigurableApplicationContext configurableApplicationContext;
    private ConfigurableEnvironment configurableEnvironment;

    private RedisConnectionRegistry redisConnectionRegistry;
    private KeyRegistry keyRegistry;
    private List<Interceptor> interceptors;
    private ClassLoader  classLoader;


    public RedisConnectionRegistry getRedisConnectionRegistry() {
        return this.redisConnectionRegistry;
    }

    public KeyRegistry getKeyRegistry() {
        return this.keyRegistry;
    }

    public List<Interceptor> getInterceptors() {
        return this.interceptors;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.configurableEnvironment = (ConfigurableEnvironment) configurableEnvironment;

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.redisConnectionRegistry = configurableApplicationContext.getBean(RedisConnectionRegistry.class);
        this.keyRegistry = configurableApplicationContext.getBean(KeyRegistry.class);
        String[] beanNamesForTypes = configurableApplicationContext.getBeanNamesForType(Interceptor.class);
        if (beanNamesForTypes != null && beanNamesForTypes.length > 0) {
            ArrayList<Interceptor> interceptors = new ArrayList<>(beanNamesForTypes.length);
            for (String beanName : beanNamesForTypes) {
                interceptors.add(configurableApplicationContext.getBean(beanName, Interceptor.class));
            }
            this.interceptors = Collections.unmodifiableList(interceptors);
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public boolean isEnabled(){
        return true;
    }

    public RedisSerializer getRedisKeyRedisSerializer(){
        return configurableApplicationContext.getBean(RedisSerializer.class);
    }

    public static RedisContext get(BeanFactory beanFactory) {
        return beanFactory.getBean(RedisContext.class);
    }
}
