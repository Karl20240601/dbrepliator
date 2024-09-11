package io.microsphere.spring.kafka.config;

import io.microsphere.spring.common.binds.config.AbstractProducerDestination;

import java.util.Map;

public class KafkaProducerDestination extends AbstractProducerDestination {
    public KafkaProducerDestination(String destinationName, Map<String, Object> configMap) {
        super(destinationName, configMap);
    }
}
