package io.microsphere.spring.common.binds.config;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractProducerDestination implements ProducerDestination{
    private  String destinationName;
    private  String applicationName;
    private  String topicPrefix;
    private Map<String,Object> configMap;


    @Override
    public String getDestinationName() {
        return this.destinationName;
    }

    @Override
    public Map<String, Object> getConfigMap() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.putAll(configMap);
        return configMap;
    }
    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    @Override
    public String getApplicationName() {
        return this.applicationName;
    }

    @Override
    public String getTopicPrefix() {
        return this.topicPrefix;
    }
}