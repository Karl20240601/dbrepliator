package io.microsphere.spring.common.binds.config;

import java.util.Map;

public interface Destination {
    String getDestinationName();
    Map<String,Object> getConfigMap();
}