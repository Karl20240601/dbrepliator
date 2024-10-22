package com.redis.test;

import com.redis.datasource.NonFunctionalRedisConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class TestConfiguration {
    @Bean("redisConnectionFactory")
    public RedisConnectionFactory createRedisConnectionFactoryRemote(){
        return new NonFunctionalRedisConnectionFactory();
    }

    @Bean
    public StringRedisTemplate createRedisTemplate(@Autowired @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory ){
        return  new StringRedisTemplate(redisConnectionFactory);
    }

}
