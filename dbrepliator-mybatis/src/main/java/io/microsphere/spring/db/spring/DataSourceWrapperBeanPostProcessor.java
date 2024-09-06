package io.microsphere.spring.db.spring;


import io.microsphere.spring.db.config.MybatisContext;
import io.microsphere.spring.db.support.wrapper.SqlSessionFactoryWrapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DataSourceWrapperBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    public static final String BEAN_NAME = "dataSourceWrapperBeanPostProcessor";

    private ConfigurableApplicationContext configurableApplicationContext;
    private MybatisContext mybatisContext;
    private Set<String> domainsSqlSessionFactoryBeanName;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        this.mybatisContext = applicationContext.getBean(MybatisContext.BEAN_NAME, MybatisContext.class);
        List<String> domainsSqlSessionFactoryBeanName = this.mybatisContext.getDbReplConfiguration().getDomainsSqlSessionFactoryBeanName();
        this.domainsSqlSessionFactoryBeanName = Collections.unmodifiableSet(new HashSet<>(domainsSqlSessionFactoryBeanName));


    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!mybatisContext.getDbReplConfiguration().isEnable()) {
            return bean;
        }
        if ((this.domainsSqlSessionFactoryBeanName.contains(beanName))) {
            if (SqlSessionFactory.class.isAssignableFrom(bean.getClass())) {
                return new SqlSessionFactoryWrapper((SqlSessionFactory) bean, mybatisContext, beanName);
            }
        }
        return bean;
    }
}
