package io.microsphere.spring.db.spring;


import io.microsphere.spring.db.config.DBReplicatorConfiguration;
import io.microsphere.spring.db.support.event.SqlSessionEventLlistener;
import io.microsphere.spring.db.support.wrapper.SqlSessionFactoryWrapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Set;

public class DataSourceWrapperBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    public static final String BEAN_NAME = "dataSourceWrapperBeanPostProcessor";

    private ConfigurableApplicationContext configurableApplicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.configurableApplicationContext = (ConfigurableApplicationContext)applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        DBReplicatorConfiguration dbReplicatorConfiguration = configurableApplicationContext.getBean(DBReplicatorConfiguration.class);
        SqlSessionEventLlistener sqlSessionEventLlistener = configurableApplicationContext.getBean(SqlSessionEventLlistener.class);
        Set<String> syncEnableDataSources = dbReplicatorConfiguration.getSyncEnableDataSources();
        if((!CollectionUtils.isEmpty(syncEnableDataSources)&&syncEnableDataSources.contains(beanName)))
        {
            if(SqlSessionFactory.class.isAssignableFrom(bean.getClass())){
                return new SqlSessionFactoryWrapper((SqlSessionFactory) bean,sqlSessionEventLlistener,beanName);
            }
        }

        return bean;
    }
}
