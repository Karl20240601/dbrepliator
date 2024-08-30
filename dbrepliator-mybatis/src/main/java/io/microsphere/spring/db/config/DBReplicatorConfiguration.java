package io.microsphere.spring.db.config;

import io.microsphere.spring.db.event.DataUpdateEventListener;
import io.microsphere.spring.db.serialize.api.Serialization;
import io.microsphere.spring.db.serialize.hessian2.AbstractHessian2FactoryInitializer;
import io.microsphere.spring.db.serialize.hessian2.Hessian2Serialization;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.*;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableMap;

public class DBReplicatorConfiguration implements ApplicationContextAware, EnvironmentAware {
    public final static String DB_REPLICATOR_PREFIX= "db.message.replicator.";
    public final static String DB_REPLICATOR_DOMAINS = DB_REPLICATOR_PREFIX+"domains";
    public final static String DB_REPLICATOR_TOPICPREFIX = "topic.prefix";
    public final static String DB_REPLICATOR_TOPICPREFIX_DEFAULT = "topic.prefix";
    public final static String DB_REPLICATOR_ENABLE = "db.replicator.enable";
    public final static String MESSAGEHANDLER_NAME_PREFIX = "messageHandler";
    public final static String MESSAGEREPLSUBSCRIBABLECHANNEL = "messageReplSubscribableChannel";
    public final static String DB_REPLICATOR_DOMAINS_KEYPREFIX = DB_REPLICATOR_DOMAINS + "keyPrefix";
    public final static String DB_REPLICATOR_DOMAINS_WRAPPER = DB_REPLICATOR_DOMAINS + "wrapper";

    private ConfigurableApplicationContext applicationContext;
    private Environment environment;
    private boolean dbReplicatorEnable;
    private Serialization serialization;

    private Map<String, SqlSessionFactory> sqlSessionFactoryMap;

    public String keyPrefix() {
        return environment.getProperty(DB_REPLICATOR_DOMAINS_KEYPREFIX);
    }


    public List<String> getDomains() {
        List property = environment.getProperty(DB_REPLICATOR_DOMAINS, List.class, emptyList());
        return property;
    }

    public List<String> getDomains(String beanName) {
        List property = environment.getProperty(DB_REPLICATOR_DOMAINS, List.class, emptyList());
        return property;
    }

    public DataUpdateEventListener getPreparedStatementEventListener() {
        return applicationContext.getBean(DataUpdateEventListener.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.dbReplicatorEnable = environment.getProperty(DB_REPLICATOR_ENABLE, boolean.class, false);
    }

    public Set<String> getSyncEnableDataSources() {
        Set<String> property = (Set<String>) this.environment.getProperty(DB_REPLICATOR_DOMAINS_WRAPPER, Set.class);
        return property;
    }

    public boolean isDbReplicatorEnable() {
        return this.dbReplicatorEnable;
    }

    public Serialization getSerialization() {
        return this.serialization;
    }


    public synchronized void init() {
        this.serialization = new Hessian2Serialization();
        initSqlSessionFactoryMap();
    }


    public void initSqlSessionFactoryMap() {
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        String[] beanNamesForType = beanFactory.getBeanNamesForType(SqlSessionFactory.class);
        Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();
        for (String beanName : beanNamesForType) {
            SqlSessionFactory bean = beanFactory.getBean(beanName, SqlSessionFactory.class);
            sqlSessionFactoryMap.put(beanName, bean);
        }
        this.sqlSessionFactoryMap = unmodifiableMap(sqlSessionFactoryMap);
        return;
    }

    public SqlSessionFactory getSqlSessionFactory(String beanName) {
        return sqlSessionFactoryMap.get(beanName);
    }

}
