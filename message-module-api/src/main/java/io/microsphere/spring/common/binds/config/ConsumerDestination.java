package io.microsphere.spring.common.binds.config;

import java.util.List;

public interface ConsumerDestination extends ProducerDestination {
    String getGroupName();
    List<String> getMessageHandlerBeanNames();
}