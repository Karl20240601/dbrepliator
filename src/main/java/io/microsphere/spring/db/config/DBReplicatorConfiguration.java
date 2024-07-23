package io.microsphere.spring.db.config;

import io.microsphere.spring.db.event.PreparedStatementEventListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DBReplicatorConfiguration implements ApplicationContextAware, EnvironmentAware {
    private ConfigurableApplicationContext applicationContext;
    private Environment environment;

    public String keyPrefix() {
        return "";
    }

    public List<String> getDomains(String beanName) {
        ArrayList arrayList = new ArrayList();
        return arrayList;
    }

    public List<String> getDomains() {
        ArrayList arrayList = new ArrayList();
        return arrayList;
    }

    public PreparedStatementEventListener getPreparedStatementEventListener() {
        return applicationContext.getBean(PreparedStatementEventListener.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    public Set<String> getSyncEnableDataSources(){
        Set<String> property = (Set<String>) this.environment.getProperty("", List.class);
        return property;
    } 
}
