package io.microsphere.spring.common.binds.config;

public interface ProducerDestination extends Destination{
    String getApplicationName();
    String getTopicPrefix();
}