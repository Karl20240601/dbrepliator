package io.microsphere.spring.annotation;

import io.microsphere.spring.common.producer.MessageContextBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MessageContextBeanDefinitionRegistrar.class)
public @interface EnableMessage  {
}
