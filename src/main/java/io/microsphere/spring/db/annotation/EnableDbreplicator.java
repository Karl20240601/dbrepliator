package io.microsphere.spring.db.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DbreplicatorBeanDefinitionRegistrar.class)
public @interface EnableDbreplicator {
}
