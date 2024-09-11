package io.microsphere.spring.common.binds.config;

import java.util.List;

public interface ConfigBuilder {
    List<ConsumerDestination>  buildConsumerDestination();
    List<ProducerDestination>  buildProducerDestination();
}
