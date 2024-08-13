package io.microsphere.spring.kafka.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration(proxyBeanMethods = false)
//@ConditionalOnClass(KafkaTemplate.class)
//@Import(HttpClientFeignConfiguration.class)
public class KafkaSubcribableChannelConfiguration {
}
