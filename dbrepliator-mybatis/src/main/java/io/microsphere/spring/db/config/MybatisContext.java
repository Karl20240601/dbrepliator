package io.microsphere.spring.db.config;

import io.microsphere.spring.db.serialize.api.Serialization;
import io.microsphere.spring.db.serialize.hessian2.Hessian2Serialization;
import io.microsphere.spring.db.support.event.SqlSessionEventLlistener;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.SmartApplicationListener;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;


public class MybatisContext implements SmartApplicationListener, ApplicationContextAware, BeanFactoryAware, SmartInitializingSingleton {
    private static final Logger logger = LoggerFactory.getLogger(MybatisContext.class);
    public static final String BEAN_NAME = "mybatisContext";
    private static final Class<SqlSessionFactory> REDIS_TEMPLATE_CLASS = SqlSessionFactory.class;

    private ConfigurableListableBeanFactory beanFactory;

    private ConfigurableApplicationContext context;

    private SqlSessionEventLlistener sqlSessionEventLlistener;

    private DbReplConfiguration dbReplConfiguration;
    private MessageConfiguration messageConfiguration;

    private Serialization serialization;

    private Map<String, SqlSessionFactory> sqlSessionFactoryMap;


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = (ConfigurableApplicationContext) applicationContext;
        this.dbReplConfiguration = beanFactory.getBean(DbReplConfiguration.BEAN_NAME, DbReplConfiguration.class);
        this.dbReplConfiguration.initProperty(this.context, context.getEnvironment());
    }


    public SqlSessionEventLlistener getSqlSessionEventLlistener() {
        return beanFactory.getBean(SqlSessionEventLlistener.class);
    }


    /**
     * 是否开启同步功能
     */
    public boolean isEnable() {
        return this.dbReplConfiguration.isEnable();
    }

    public Serialization getSerialization() {
        return this.serialization;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ContextRefreshedEvent.class.equals(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            onContextRefreshedEvent((ContextRefreshedEvent) event);
        }
    }

    private void onContextRefreshedEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        this.messageConfiguration.init(context, this.dbReplConfiguration);
        initSqlSessionFactoryMap(this.context);
    }

    public DbReplConfiguration getDbReplConfiguration() {
        return dbReplConfiguration;
    }

    public MessageConfiguration getMessageConfiguration() {
        return messageConfiguration;
    }

    public String getContextDomains() {
        return this.dbReplConfiguration.getContextDomains(this.context.getEnvironment());
    }

    @Override
    public void afterSingletonsInstantiated() {
        this.sqlSessionEventLlistener = getSqlSessionEventLlistener();
        this.messageConfiguration = beanFactory.getBean(MessageConfiguration.BEAN_NAME, MessageConfiguration.class);
        this.serialization = new Hessian2Serialization();
    }

    public void initSqlSessionFactoryMap(ConfigurableApplicationContext applicationContext) {
        String[] beanNamesForType = applicationContext.getBeanNamesForType(SqlSessionFactory.class);
        Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();
        for (String beanName : beanNamesForType) {
            SqlSessionFactory bean = applicationContext.getBean(beanName, SqlSessionFactory.class);
            sqlSessionFactoryMap.put(beanName, bean);
        }
        this.sqlSessionFactoryMap = unmodifiableMap(sqlSessionFactoryMap);
    }

    public SqlSessionFactory getSqlSessionFactory(String beanName) {
        return sqlSessionFactoryMap.get(beanName);
    }
}
