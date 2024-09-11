package io.microsphere.spring.common.binds.config;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractProducerDestination implements ProducerDestination{
    private  String destinationName;
    private Map<String,Object> configMap;

    public AbstractProducerDestination(String destinationName, Map<String, Object> configMap) {
        this.destinationName = destinationName;
        this.configMap = configMap;
    }

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
}