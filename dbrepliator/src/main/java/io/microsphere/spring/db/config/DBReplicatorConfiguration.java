package io.microsphere.spring.db.config;

import io.microsphere.spring.db.event.DataUpdateEventListener;
import io.microsphere.spring.db.serialize.api.Serialization;
import io.microsphere.spring.db.serialize.hessian2.AbstractHessian2FactoryInitializer;
import io.microsphere.spring.db.serialize.hessian2.Hessian2Serialization;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.messaging.MessageChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

public class DBReplicatorConfiguration implements ApplicationContextAware, EnvironmentAware {
    public final static String DB_REPLICATOR_DOMAINS = "db.message.replicator.domains";
    public final static String MESSAGEHANDLER_NAME_PREFIX = "messageHandler";
    public final static String MESSAGEREPLSUBSCRIBABLECHANNEL = "messageReplSubscribableChannel";

    private ConfigurableApplicationContext applicationContext;
    private Environment environment;
    private boolean dbReplicatorEnable;

    public String keyPrefix() {
        return "";
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
        this.dbReplicatorEnable = environment.getProperty("", boolean.class, false);
    }

    public Set<String> getSyncEnableDataSources() {
        Set<String> property = (Set<String>) this.environment.getProperty("db.mssage.replicator.domains.wrapper", Set.class);
        return property;
    }

    public boolean isDbReplicatorEnable() {
        return this.dbReplicatorEnable;
    }

    public Serialization getSerialization() {
        AbstractHessian2FactoryInitializer.getSerializerFactory1();
        return new Hessian2Serialization();
    }

}
