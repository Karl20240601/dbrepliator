package io.microsphere.spring.common.binds.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractConsumberDestination implements ConsumerDestination {
    private String groupName;
    private List<String> messageHandlerBeanNames;
    private Map<String, Object> configMap;
    private String destinationName;

    public AbstractConsumberDestination(String destinationName, String groupName, List<String> getMessageHandlerBeanNames, Map<String, Object> configMap) {
        this.groupName = groupName;
        this.messageHandlerBeanNames = getMessageHandlerBeanNames;
        this.configMap = configMap;
        this.destinationName = destinationName;
    }

    public AbstractConsumberDestination() {
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public List<String> getMessageHandlerBeanNames() {
        ArrayList<String> objects = new ArrayList<>(messageHandlerBeanNames.size());
        objects.addAll(messageHandlerBeanNames);
        return this.messageHandlerBeanNames;
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

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setMessageHandlerBeanNames(List<String> messageHandlerBeanNames) {
        this.messageHandlerBeanNames = messageHandlerBeanNames;
    }

    public void setConfigMap(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
}