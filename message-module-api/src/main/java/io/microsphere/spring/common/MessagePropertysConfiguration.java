package io.microsphere.spring.common;

import org.springframework.core.env.ConfigurableEnvironment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.microsphere.spring.util.PropertySourcesUtils.getSubProperties;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG;

public class MessagePropertysConfiguration {

    public static final String PROPERTY_NAME_PREFIX = "mssage.replicator.";
    public static final String PRODUCER_PROPERTY_NAME_PREFIX = PROPERTY_NAME_PREFIX + "producer.";
    public static final String CONSUMER_PROPERTY_NAME_PREFIX = PROPERTY_NAME_PREFIX + "consumer.";



    public static Map<String, Object> initConsumerConfigs(ConfigurableEnvironment environment, String groupId, List<String> brokerList) {
        Map<String, Object> consumerConfigs = new HashMap<>();

        // Kafka bootstrap servers
        consumerConfigs.put(BOOTSTRAP_SERVERS_CONFIG, brokerList);
        // Kafka consumer group id
        consumerConfigs.put(GROUP_ID_CONFIG, groupId);

        // Kafka Common properties
        consumerConfigs.putAll(getSubProperties(environment, PROPERTY_NAME_PREFIX));

        // Kafka Consumer properties
        consumerConfigs.putAll(getSubProperties(environment, CONSUMER_PROPERTY_NAME_PREFIX));
        return consumerConfigs;
    }

    public static Map<String, Object> initProducerConfigs(ConfigurableEnvironment environment, String groupId, List<String> brokerList) {
        Map<String, Object> consumerConfigs = new HashMap<>();

        // Kafka bootstrap servers
        consumerConfigs.put(BOOTSTRAP_SERVERS_CONFIG, brokerList);
        // Kafka Common properties
        consumerConfigs.putAll(getSubProperties(environment, PROPERTY_NAME_PREFIX));

        // Kafka Consumer properties
        consumerConfigs.putAll(getSubProperties(environment, PRODUCER_PROPERTY_NAME_PREFIX));
        return consumerConfigs;
    }
}
