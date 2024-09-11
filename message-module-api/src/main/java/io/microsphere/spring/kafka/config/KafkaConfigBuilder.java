package io.microsphere.spring.kafka.config;

import io.microsphere.spring.common.MessagePropertysConfiguration;
import io.microsphere.spring.common.binds.config.ConfigBuilder;
import io.microsphere.spring.common.binds.config.ConsumerDestination;
import io.microsphere.spring.common.binds.config.ProducerDestination;
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
    private final List<String> producertDomains;

    public KafkaConfigBuilder(Environment environment) {
        this.environment = environment;
        List<String> propertys = this.environment.getProperty(CONSUMER_TOPIC_CONFIG_DOMAIN_PROPERTIES, List.class, Collections.emptyList());
        this.consumberDomains = propertys;
        List<String> producertDomains = this.environment.getProperty(PRODUCER_TOPIC_CONFIG_DOMAINS, List.class, Collections.emptyList());
        this.producertDomains = producertDomains;
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

    @Override
    public List<ProducerDestination> buildProducerDestination() {
        if (CollectionUtils.isEmpty(producertDomains)) {
            return Collections.emptyList();
        }
        ArrayList<ProducerDestination> objects = new ArrayList<>(consumberDomains.size());
        for (String domain : this.producertDomains) {
            ProducerDestination kafkaProducerDestination = buildProducerDestination(domain, this.environment);
            objects.add(kafkaProducerDestination);
        }
        return objects;
    }


    private ConsumerDestination buildConsumerDestination(String domain, Environment environment) {
        String groupProperty = CONSUMER_TOPIC_CONFIG_DOMAIN_PROPERTIES + domain + GROUP_PROPERTY;
        String messageHandlerBeanNameProperty = CONSUMER_TOPIC_CONFIG_DOMAIN_PROPERTIES + domain + MESSAGEHANDLER_BEAN_NAME_PROPERTY;
        List<String> messageHandlerBeanNames = environment.getProperty(messageHandlerBeanNameProperty, List.class, Collections.emptyList());
        String group = environment.getProperty(groupProperty, String.class);

        String topicPrefixProperty =  PROPERTY_NAME_PREFIX+ domain + TOPIC_PREFIX_PROPERTY;
        String topicPrefix = environment.getProperty(topicPrefixProperty, String.class,"");

        Map<String, Object> objectMap = initConsumerConfigs((ConfigurableEnvironment) environment);
        return new KafkaConsumerDestination(topicPrefix + domain, group,messageHandlerBeanNames, objectMap);
    }


    private ProducerDestination buildProducerDestination(String domain, Environment environment) {
        String topicPrefixProperty = PROPERTY_NAME_PREFIX + domain + TOPIC_PREFIX_PROPERTY;
        String topicPrefix = environment.getProperty(topicPrefixProperty, String.class,"");
        Map<String, Object> objectMap = initProducerConfigs((ConfigurableEnvironment) environment);
        return new KafkaProducerDestination(topicPrefix + domain, objectMap);
    }
}
