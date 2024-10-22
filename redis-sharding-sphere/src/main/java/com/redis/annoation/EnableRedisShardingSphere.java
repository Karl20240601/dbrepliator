package com.redis.annoation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisShardingSphereBeanDefinitionRegistrar.class)
public @interface  EnableRedisShardingSphere {
}
