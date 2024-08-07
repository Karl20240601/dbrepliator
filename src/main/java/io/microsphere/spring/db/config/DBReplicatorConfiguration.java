package io.microsphere.spring.db.config;

import io.microsphere.spring.db.event.DataUpdateEventListener;
import io.microsphere.spring.db.serialize.api.Serialization;
import io.microsphere.spring.db.serialize.hessian2.AbstractHessian2FactoryInitializer;
import io.microsphere.spring.db.serialize.hessian2.Hessian2Serialization;
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
    private  boolean dbReplicatorEnable;
    public String keyPrefix() {
        return "";
    }

    public List<String> getDomains(String beanName) {
        ArrayList arrayList = new ArrayList();
        arrayList.add("test1");
//        arrayList.add("test2");
//        arrayList.add("test3");
        return arrayList;
    }

    public List<String> getDomains() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("test1");
        arrayList.add("test2");
        arrayList.add("test3");
        return arrayList;
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
        this.dbReplicatorEnable = environment.getProperty("",boolean.class,false);
    }
    
    public Set<String> getSyncEnableDataSources(){
        Set<String> property = (Set<String>) this.environment.getProperty("", List.class);
        return property;
    }

    public boolean isDbReplicatorEnable(){
        return this.dbReplicatorEnable;
    }

    public Serialization getSerialization(){
        AbstractHessian2FactoryInitializer.getSerializerFactory1();
        return new Hessian2Serialization();
    }
}
