package io.microsphere.spring.kafka;

import java.util.HashMap;
import java.util.Map;


import io.microsphere.spring.common.producer.AbstractMessageChannelFactory;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.MessageChannel;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;

public class KafkaMessageChannelFactory extends AbstractMessageChannelFactory {
    private ProducerFactory<byte[], byte[]> producerFactory;
    private Map<String, Object> producerConfigs;


    @Override
    public MessageChannel createMessageChannel(String topics) {
        KafkaTemplate kafkaTemplate = null;
        try {
            kafkaTemplate = this.configurableApplicationContext.getBean(KafkaTemplate.class);
        } catch (NoSuchBeanDefinitionException e) {

        }

        if (kafkaTemplate == null) {
            this.producerFactory = createReplicatorProducerFactory();
            kafkaTemplate = new KafkaTemplate<>(producerFactory);
        }

        return new KafkaMessageChannel(kafkaTemplate, topics);
    }

    private ProducerFactory<byte[], byte[]> createReplicatorProducerFactory() {
        DefaultKafkaProducerFactory producerFactory = new DefaultKafkaProducerFactory<>(getMessageConfig());
        producerFactory.setKeySerializer(new ByteArraySerializer());
        producerFactory.setValueSerializer(new ByteArraySerializer());
        return producerFactory;
    }

    public Map<String, Object> getMessageConfig() {
        Map<String, Object> producerConfigs = new HashMap<>();
        producerConfigs.put(BOOTSTRAP_SERVERS_CONFIG, this.environment.getProperty(KAFKA_CONFIG_PROPERTIES));
        this.producerConfigs = producerConfigs;
        return producerConfigs;
    }

    @Override
    public void destroy() throws Exception {
        if (producerFactory != null) {
            producerFactory.reset();
        }

    }
}
