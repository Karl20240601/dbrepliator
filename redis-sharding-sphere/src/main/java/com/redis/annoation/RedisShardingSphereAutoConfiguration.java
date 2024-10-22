package com.redis.annoation;

import com.redis.datasource.LettuceRedisConnectionFactoryProvider;
import com.redis.datasource.RedisConnectionFactoryProvider;
import com.redis.datasource.config.RedisHostConfigProperties;
import com.redis.interceptor.Interceptor;
import com.redis.interceptor.LogInterceptor;
import com.redis.registy.DefaultKeyRegistry;
import com.redis.registy.DefaultRedisConnectionRegistry;
import com.redis.registy.KeyRegistry;
import com.redis.registy.RedisConnectionRegistry;
import com.redis.router.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties({RedisHostConfigProperties.class, RedisProperties.class})
public class RedisShardingSphereAutoConfiguration {

    @Bean
//    @ConditionalOnBean({RedisConnectionFactoryProvider.class,RedisProperties.class,RedisHostConfigProperties.class})
    public RedisConnectionRegistry createRedisConnectionRegistry(RedisProperties redisProperties, RedisHostConfigProperties redisHostConfigProperties, RedisConnectionFactoryProvider redisConnectionFactoryProvider) {
        return new DefaultRedisConnectionRegistry(redisProperties, redisHostConfigProperties, redisConnectionFactoryProvider);
    }

    @Bean
    @ConditionalOnClass(name = {"org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory"})
    public RedisConnectionFactoryProvider createRedisConnectionFactoryProvider() {
        return new LettuceRedisConnectionFactoryProvider();
    }

    @Bean
    public KeyRegistry createKeyRegistry() {
        return new DefaultKeyRegistry();
    }

    @Bean
    public Interceptor createLogInterceptor() {
        return new LogInterceptor();
    }

    @Bean
    public Selector createSelector() {
        return new RoundRobinLoadSelector();
    }

    @Bean
    public RouterChain createRouterChain(List<Router> routerList) {
        DefaultRouterChain defaultRouterChain = new DefaultRouterChain();
        defaultRouterChain.addRouter(routerList);
        return defaultRouterChain;
    }

    @Bean(name="tagRouter")
    public Router createTagRouter() {
        return new TagRouter();
    }
    @Bean(name="readOrWrieteRouter")
    public Router createReadOrWrieteRouter() {
        return new ReadOrWrieteRouter();
    }
}
