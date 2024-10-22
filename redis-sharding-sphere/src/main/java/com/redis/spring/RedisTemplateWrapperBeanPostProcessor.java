package com.redis.spring;


import com.redis.RedisContext;
import com.redis.proxy.RedisTemplateWrapper;
import com.redis.proxy.StringRedisTemplateWrapper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;


import static org.springframework.aop.framework.AopProxyUtils.ultimateTargetClass;

/**
 * {@link BeanPostProcessor} implements Wrapper {@link RedisTemplate} and {@link StringRedisTemplate}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see RedisTemplateWrapper
 * @see StringRedisTemplateWrapper
 * @see BeanPostProcessor
 * @since 1.0.0
 */
public class RedisTemplateWrapperBeanPostProcessor implements BeanPostProcessor, InitializingBean, ApplicationContextAware {

    public static final String BEAN_NAME = "redisTemplateWrapperBeanPostProcessor";

    private ConfigurableApplicationContext context;

    private RedisContext redisContext;



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = ultimateTargetClass(bean);
        if (StringRedisTemplate.class.equals(beanClass)) {
            StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) bean;
            return new StringRedisTemplateWrapper(stringRedisTemplate, redisContext);
        } else if (RedisTemplate.class.equals(beanClass)) {
            RedisTemplate redisTemplate = (RedisTemplate) bean;
            return new RedisTemplateWrapper(redisTemplate, redisContext);
        }
        return bean;
    }


    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        Assert.isInstanceOf(ConfigurableApplicationContext.class, context, "The 'context' argument must be an instance of ConfigurableApplicationContext");
        this.context = (ConfigurableApplicationContext) context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.redisContext = RedisContext.get(context);
    }
}
