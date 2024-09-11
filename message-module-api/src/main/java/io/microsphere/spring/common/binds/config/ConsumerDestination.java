package io.microsphere.spring.common.binds.config;

import java.util.List;

public interface ConsumerDestination extends Destination {
    String getGroupName();
    List<String> getMessageHandlerBeanNames();
}