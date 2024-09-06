package io.microsphere.spring.db.config;


import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import java.util.*;

import static java.util.Collections.*;

public class DbReplConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DbReplConfiguration.class);

    public final static String BEAN_NAME = "dbReplConfiguration";
    public final static String DB_REPLICATOR_PREFIX = "db.replicator.";
    public final static String DB_REPLICATOR_ENABLE = DB_REPLICATOR_PREFIX + "enable";
    public final static String DB_REPLICATOR_DOMAINS = DB_REPLICATOR_PREFIX + "domains";
    /**
     * 当前业务名称
     */
    public final static String DB_REPLICATOR_DOMAIN = DB_REPLICATOR_PREFIX + "domain";
    /**
     * 需要同步业务域名
     */
    public final static String DB_REPLICATOR_SQLSESSIONFACTORY_BEAN_NAME = DB_REPLICATOR_PREFIX + "domains";
    public final static String DB_REPLICATOR_SQLSESSIONFACTORY_BEAN_NAME_SUFFIX = DB_REPLICATOR_PREFIX + "wrapper";
    public final static String DEFAULT_DOMAIN = "default";
    public static final List<String> DEFAULT_DOMAINS = Arrays.asList(DEFAULT_DOMAIN);


    private boolean enable;
    /**
     * 同步业务域名
     */
    private List<String> domains;
    private Map<String, List<String>> targetDomains;
    /**
     * db.replicator.domains.app1.sql-session-factory-bean-name=sqlSessionFactoryBeanName
     */
    private List<String> domainsSqlSessionFactoryBeanName;





    public void initProperty(ConfigurableApplicationContext applicationContext, Environment environment) {
        setDomains(environment);
        setSourceBeanDomains(environment, this.domains);
        setDomainsSqlSessionFactoryBeanName(environment, this.domains);
        this.enable = environment.getProperty(DB_REPLICATOR_ENABLE, boolean.class, false);

    }

    @NonNull
    public List<String> getDomains(Environment environment) {
        return unmodifiableList(environment.getProperty(DB_REPLICATOR_DOMAINS, List.class, DEFAULT_DOMAINS));
    }

    @NonNull
    public List<String> getWrapperBeanNamesPropertyName(Environment environment, String domain) {
        String propertyName = getWrapperBeanNamesPropertyName(domain);
        return unmodifiableList(environment.getProperty(propertyName, List.class, emptyList()));
    }

    private String getWrapperBeanNamesPropertyName(String domain) {
        return DB_REPLICATOR_SQLSESSIONFACTORY_BEAN_NAME + domain + DB_REPLICATOR_SQLSESSIONFACTORY_BEAN_NAME_SUFFIX;
    }

    private void setDomains(Environment environment) {
        List<String> domains = getDomains(environment);
        setSourceBeanDomains(environment, domains);
        this.domains = domains;
    }


    private void setSourceBeanDomains(Environment environment, List<String> domains) {
        Map<String, List<String>> sourceBeanDomains = new HashMap<>(domains.size());
        for (String domain : domains) {
            for (String sqlSessionFactoryBeanName : getWrapperBeanNamesPropertyName(environment, domain)) {
                List<String> beanDomains = sourceBeanDomains.computeIfAbsent(sqlSessionFactoryBeanName, n -> new LinkedList<>());
                if (!beanDomains.contains(domain)) {
                    beanDomains.add(domain);
                } else {
                    logger.warn("RedisTemplate Bean[name :{}] is repeatedly associated with domain :{}, this configuration will be ignored!", sqlSessionFactoryBeanName, domain);
                }
            }
        }
        this.targetDomains = unmodifiableMap(sourceBeanDomains);
    }

    private void setDomainsSqlSessionFactoryBeanName(Environment environment, List<String> domains) {
        List<String> property = environment.getProperty(DB_REPLICATOR_SQLSESSIONFACTORY_BEAN_NAME_SUFFIX, List.class, emptyList());
        this.domainsSqlSessionFactoryBeanName = unmodifiableList(property);
    }


    public List<String> getDomainsSqlSessionFactoryBeanName() {
        return this.domainsSqlSessionFactoryBeanName;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<String> getDomains() {
        return domains;
    }

    public String getContextDomains(Environment environment) {
        return environment.getProperty(DB_REPLICATOR_DOMAIN, String.class);
    }




}
