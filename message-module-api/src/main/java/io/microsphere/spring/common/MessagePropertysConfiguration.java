package io.microsphere.spring.common;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static io.microsphere.spring.util.PropertySourcesUtils.getSubProperties;
import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;

public class MessagePropertysConfiguration {

    public static final String PROPERTY_NAME_PREFIX = "message.replicator.";
    public static final String PROPERTY_NAME_PREFIX_APPLICATION = PROPERTY_NAME_PREFIX + "applications";
    public static final String PROPERTY_NAME_BOOT_SERVER = PROPERTY_NAME_PREFIX + ".bootstrap.servers";
    public static final String PRODUCER_PROPERTY_NAME_PREFIX = PROPERTY_NAME_PREFIX_APPLICATION + "producer.";
    public static final String CONSUMER_PROPERTY_NAME_PREFIX = PROPERTY_NAME_PREFIX_APPLICATION + "consumer.";

    public final static String KAFKA_CONFIG_PROPERTIES = "spring.kafka.bootstrap-servers";
    public final static String RABBITMQ_CONFIG_PROPERTIES = "spring.kafka.bootstrap-servers";
    public final static String TOPIC_CONFIG_PROPERTIES = "spring.kafka.bootstrap-servers";
    public final static String CONSUMER_TOPIC_CONFIG_DOMAIN_PROPERTIES = PROPERTY_NAME_PREFIX_APPLICATION + "domain.";
    public final static String PRODUCER_TOPIC_CONFIG_DOMAINS = PROPERTY_NAME_PREFIX_APPLICATION +".%s."+ "domains.";
    public final static String PARTION_PROPERTY = ".partion";
    public final static String GROUP_PROPERTY = ".group";
    public final static String MESSAGEHANDLER_BEAN_NAME_PROPERTY = ".messagehander";
    public final static String TOPIC_PREFIX_PROPERTY = PROPERTY_NAME_PREFIX_APPLICATION+".%s."+".topic.prefix";


    public static Map<String, Object> initConsumerConfigs(ConfigurableEnvironment environment) {
        Map<String, Object> consumerConfigs = new HashMap<>();

        // Kafka bootstrap servers
        consumerConfigs.put(BOOTSTRAP_SERVERS_CONFIG, getBootServerList(environment));
        // Kafka consumer group id
        //consumerConfigs.put(GROUP_ID_CONFIG, groupId);

        // Kafka Common properties
        consumerConfigs.putAll(getSubProperties(environment, PROPERTY_NAME_PREFIX));

        // Kafka Consumer properties
        consumerConfigs.putAll(getSubProperties(environment, CONSUMER_PROPERTY_NAME_PREFIX));
        return consumerConfigs;
    }

    public static Map<String, Object> initProducerConfigs(ConfigurableEnvironment environment) {
        Map<String, Object> consumerConfigs = new HashMap<>();
        List<String> bootServerList = getBootServerList(environment);
        // Kafka bootstrap servers
        consumerConfigs.put(BOOTSTRAP_SERVERS_CONFIG, bootServerList);
        // Kafka Common properties
        consumerConfigs.putAll(getSubProperties(environment, PROPERTY_NAME_PREFIX));

        // Kafka Consumer properties
        consumerConfigs.putAll(getSubProperties(environment, PRODUCER_PROPERTY_NAME_PREFIX));
        return consumerConfigs;
    }

    private static List<String> getBootServerList(ConfigurableEnvironment environment) {

        String property = environment.getProperty(PROPERTY_NAME_BOOT_SERVER);
        if (StringUtils.hasText(property)) {
            return getServerList(property);
        }
        property = environment.getProperty(KAFKA_CONFIG_PROPERTIES);
        if (StringUtils.hasText(property)) {
            return getServerList(property);
        }
        return null;
    }

    private static List<String> getServerList(String property) {
        String[] split = StringUtils.split(property, ",");
        return Arrays.asList(split);
    }



    public static List<String> getApplications(ConfigurableEnvironment environment) {
        List<String> property = environment.getProperty(PROPERTY_NAME_PREFIX_APPLICATION,
                List.class,
                Collections.emptyList());
        return property;
    }
}
