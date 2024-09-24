package io.microsphere.dynamic.jdbc.spring.boot.namespaces;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;

public class NamespaceBeanNameGenerator implements BeanNameGenerator {
    private static final String NAME_SPACE = "namespace[%s]";
    private final String namespace;

    public NamespaceBeanNameGenerator(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        String beanName = DefaultBeanNameGenerator.INSTANCE.generateBeanName(definition, registry);
        return String.format(NAME_SPACE,namespace)+"."+beanName;
    }
}
