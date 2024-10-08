package com.crazymaker.gateway.core.api.filterchain;

import java.lang.annotation.*;

/**
 * 过滤器注解类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FilterAnnotation {

    /**
     * 过滤器的唯一ID
     */
    String id() default "";


    String name() default "";


    ProcessorFilterType type();

    int order() default 0; // 值越大，优先级越低， 在责任链上 ，排在后面


    boolean global() default true;


}
