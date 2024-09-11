package io.microsphere.spring.kafka.config;

import io.microsphere.spring.common.binds.config.AbstractConsumberDestination;

import java.util.List;
import java.util.Map;

public class KafkaConsumerDestination extends AbstractConsumberDestination {
    public KafkaConsumerDestination(String destinationName, String groupName, List<String> getMessageHandlerBeanNames, Map<String, Object> configMap) {
        super(destinationName,groupName,getMessageHandlerBeanNames,configMap);
    }


}
