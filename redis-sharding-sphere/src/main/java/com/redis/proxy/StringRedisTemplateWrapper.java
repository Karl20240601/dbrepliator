package com.redis.proxy;

import com.redis.RedisContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static com.redis.proxy.RedisTemplateWrapper.REDIS_CONNECTION_TYPES;


public class StringRedisTemplateWrapper extends StringRedisTemplate implements DelegatingWrapper {

    private final StringRedisTemplate delegate;

    private final RedisContext redisContext;

    public StringRedisTemplateWrapper(StringRedisTemplate delegate, RedisContext redisContext) {
        this.delegate = delegate;
        this.redisContext = redisContext;
        init();
    }

    private void init() {
        configure(delegate, this);
    }
    private void configure(RedisTemplate<?, ?> source, RedisTemplate<?, ?> target) {
        // Set the connection
        target.setConnectionFactory(source.getConnectionFactory());
        target.setExposeConnection(source.isExposeConnection());

        // Set the RedisSerializers
        target.setEnableDefaultSerializer(source.isEnableDefaultSerializer());
        target.setDefaultSerializer(source.getDefaultSerializer());
        target.setKeySerializer(source.getKeySerializer());
        target.setValueSerializer(source.getValueSerializer());
        target.setHashKeySerializer(source.getHashKeySerializer());
        target.setHashValueSerializer(source.getHashValueSerializer());
        target.setStringSerializer(source.getStringSerializer());
    }
    @Override
    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        if (isEnabled()) {
            return newProxyRedisConnection(connection, redisContext);
        }
        return connection;
    }

    protected  RedisConnection newProxyRedisConnection(RedisConnection connection, RedisContext redisContext) {
        ClassLoader classLoader = redisContext.getClassLoader();
        InvocationHandler invocationHandler = newInvocationHandler(connection, redisContext);
        return (RedisConnection) Proxy.newProxyInstance(classLoader, REDIS_CONNECTION_TYPES, invocationHandler);
    }

    private  InvocationHandler newInvocationHandler(RedisConnection connection, RedisContext redisContext) {
        return new InterceptingRedisConnectionInvocationHandler(this.delegate.getKeySerializer(),connection, redisContext);
    }

    public boolean isEnabled() {
        return redisContext.isEnabled();
    }

    @Override
    public Object getDelegate() {
        return delegate;
    }
}
