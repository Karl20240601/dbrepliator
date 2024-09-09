package io.microsphere.spring.common.binds.config;

import java.util.Map;
public interface ProducerDestination {
    String getDestinationName();
    Map<String,Object> getConfigMap();
}