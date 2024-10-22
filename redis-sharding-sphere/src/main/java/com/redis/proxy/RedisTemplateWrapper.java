package com.redis.proxy;

import com.redis.RedisContext;
import com.redis.registy.KeyRegistry;
import com.redis.registy.RedisConnectionRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.redis.connection.RedisCommands;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * {@link RedisTemplate} Wrapper class, compatible with {@link RedisTemplate}
 *
 * @param <K> Redis Key type
 * @param <V> Redis Value type
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
public class RedisTemplateWrapper<K, V> extends RedisTemplate<K, V> implements DelegatingWrapper {

    public static final Class<?>[] REDIS_CONNECTION_TYPES = new Class[]{RedisConnection.class};


    private final RedisTemplate<K, V> delegate;
    private RedisContext redisContext;


    public RedisTemplateWrapper(RedisTemplate<K, V> delegate, RedisContext context) {
        this.delegate = delegate;
        this.redisContext = context;

        init();
    }

    private void init() {
        configure(delegate, this);
    }

    static void configure(RedisTemplate<?, ?> source, RedisTemplate<?, ?> target) {
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
        return newProxyRedisConnection(connection);

    }


    protected  RedisConnection newProxyRedisConnection(RedisConnection connection) {
        ClassLoader classLoader = RedisConnection.class.getClassLoader();
        InvocationHandler invocationHandler = newInvocationHandler(connection);
        return (RedisConnection) Proxy.newProxyInstance(classLoader, REDIS_CONNECTION_TYPES, invocationHandler);
    }

    private  InvocationHandler newInvocationHandler(RedisConnection connection) {
        return new InterceptingRedisConnectionInvocationHandler(delegate.getKeySerializer(),connection,redisContext);
    }

    @Override
    public Object getDelegate() {
        return delegate;
    }
}
