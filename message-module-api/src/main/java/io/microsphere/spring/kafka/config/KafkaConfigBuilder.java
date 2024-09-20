package io.microsphere.spring.kafka.config;

import io.microsphere.spring.common.binds.config.Application;
import io.microsphere.spring.common.binds.config.ConfigBuilder;
import io.microsphere.spring.common.binds.config.ConsumerDestination;
import io.microsphere.spring.common.binds.config.ProducerDestination;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.microsphere.spring.common.MessagePropertysConfiguration.*;

public class KafkaConfigBuilder implements ConfigBuilder {
    private final Environment environment;
    private final List<String> consumberDomains;

    public KafkaConfigBuilder(Environment environment) {
        this.environment = environment;
        List<String> propertys = this.environment.getProperty(CONSUMER_TOPIC_CONFIG_DOMAIN_PROPERTIES, List.class, Collections.emptyList());
        this.consumberDomains = propertys;
    }


    @Override
    public List<ConsumerDestination> buildConsumerDestination() {
        if (CollectionUtils.isEmpty(consumberDomains)) {
            return Collections.emptyList();
        }
        ArrayList<ConsumerDestination> objects = new ArrayList<>(consumberDomains.size());
        for (String domain : consumberDomains) {
            ConsumerDestination consumerDestination = buildConsumerDestination(domain, this.environment);
            objects.add(consumerDestination);
        }
        return objects;
    }


    private ConsumerDestination buildConsumerDestination(String domain, Environment environment) {
        String groupProperty = CONSUMER_TOPIC_CONFIG_DOMAIN_PROPERTIES + domain + GROUP_PROPERTY;
        String messageHandlerBeanNameProperty = CONSUMER_TOPIC_CONFIG_DOMAIN_PROPERTIES + domain + MESSAGEHANDLER_BEAN_NAME_PROPERTY;
        List<String> messageHandlerBeanNames = environment.getProperty(messageHandlerBeanNameProperty, List.class, Collections.emptyList());
        String group = environment.getProperty(groupProperty, String.class);

        String topicPrefixProperty = PROPERTY_NAME_PREFIX + domain + TOPIC_PREFIX_PROPERTY;
        String topicPrefix = environment.getProperty(topicPrefixProperty, String.class, "");

        Map<String, Object> objectMap = initConsumerConfigs((ConfigurableEnvironment) environment);
        return new KafkaConsumerDestination(topicPrefix + domain, group, messageHandlerBeanNames, objectMap);
    }

    @Override
    public List<ProducerDestination> buildProducerDestination() {
        List<String> produceApplications = this.environment.getProperty(PRODUCER_TOPIC_CONFIG_DOMAINS, List.class, Collections.emptyList());
        if (CollectionUtils.isEmpty(produceApplications)) {
            return Collections.emptyList();
        }
        List<ProducerDestination> producerDestinations = new ArrayList<>();

        for (String application : produceApplications) {
            String topicPrefixProperty = this.environment.getProperty(String.format(TOPIC_PREFIX_PROPERTY, application), application);
            List<String> domains = this.environment.getProperty(String.format(PRODUCER_TOPIC_CONFIG_DOMAINS, application), List.class, Collections.emptyList());
            Map<String, Object> objectMap = initConsumerConfigs((ConfigurableEnvironment) this.environment);
            for (String domain : domains) {
                KafkaProducerDestination kafkaProducerDestination = new KafkaProducerDestination();
                kafkaProducerDestination.setApplicationName(application);
                kafkaProducerDestination.setConfigMap(objectMap);
                if (StringUtils.isNotEmpty(topicPrefixProperty)) {
                    kafkaProducerDestination.setDestinationName(String.format("%s%s", topicPrefixProperty, domain));
                } else {
                    kafkaProducerDestination.setDestinationName(String.format("%s-%s", application, domain));
                }
                producerDestinations.add(kafkaProducerDestination);
            }

        }
        return producerDestinations;
    }
}
