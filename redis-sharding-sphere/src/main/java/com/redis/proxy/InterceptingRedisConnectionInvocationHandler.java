package com.redis.proxy;

import com.redis.RedisContext;
import com.redis.TagRedisConnectionNode;
import com.redis.interceptor.Interceptor;
import com.redis.metadata.MethodMetadata;
import com.redis.registy.KeyRegistry;
import com.redis.RedisMethodContext;
import com.redis.metadata.RedisMetadataRepository;
import com.redis.registy.RedisConnectionRegistry;
import com.redis.router.Contanst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * {@link InvocationHandler} for Intercepting {@link RedisConnection}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
public class InterceptingRedisConnectionInvocationHandler implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(InterceptingRedisConnectionInvocationHandler.class);

    private static final String HASH_CODE = "hashCode";

    private static final String EQUALS = "equals";

    private final RedisConnection rawRedisConnection;


    private List<Interceptor> interceptors;

    private final KeyRegistry keyRegistry;

    private final RedisSerializer keyRedisSerializer;
    private final RedisConnectionRegistry redisConnectionRegistry;

    private RedisContext redisContext;


    public InterceptingRedisConnectionInvocationHandler(RedisSerializer keyRedisSerializer, RedisConnection rawRedisConnection, RedisContext redisContext) {
        this.rawRedisConnection = rawRedisConnection;
        this.keyRedisSerializer = keyRedisSerializer;
        this.redisConnectionRegistry = redisContext.getRedisConnectionRegistry();
        this.keyRegistry = redisContext.getKeyRegistry();
        this.interceptors = redisContext.getInterceptors();
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();

        if (EQUALS.equals(methodName)) {
            // Only consider equal when proxies are identical.
            return (proxy == args[0]);
        } else if (HASH_CODE.equals(methodName)) {
            // Use hashCode of PersistenceManager proxy.
            return System.identityHashCode(proxy);
        }else if(args == null) {
            return ReflectionUtils.invokeMethod(method, rawRedisConnection, args);
        }

        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        MethodMetadata methodMetadata = RedisMetadataRepository.findMethodMetadata(method);
        RedisMethodContext redisMethodContext = null;
        String redisKey = (String) keyRedisSerializer.deserialize((byte[]) args[0]);
        if (methodMetadata.isWrite()) {
            redisMethodContext = new RedisMethodContext(redisKey, Contanst.WRITE, keyRegistry.getTag(redisKey));
        } else {
            redisMethodContext = new RedisMethodContext(redisKey, Contanst.READ, keyRegistry.getTag(redisKey));
        }
        Object result = null;
        Throwable failure = null;
        try {
            beforeExecute(redisMethodContext);
            TagRedisConnectionNode select = redisConnectionRegistry.select(redisMethodContext);
            if (select != null) {
                RedisConnection redisConnection = select.getRedisConnection();
                result = ReflectionUtils.invokeMethod(method, redisConnection, args);
            }
        } catch (Throwable e) {
            failure = e;
            throw e.getCause();
        } finally {
            afterExecute(redisMethodContext, result, failure);
        }
        return result;
    }


    private void beforeExecute(RedisMethodContext redisMethodContext) {
        beforeExecute(this.interceptors, redisMethodContext);
    }

    private void beforeExecute(List<Interceptor> interceptors, RedisMethodContext redisMethodContext) {
        for (int i = 0; i < interceptors.size(); i++) {
            Interceptor interceptor = interceptors.get(i);
            interceptor.before(redisMethodContext);
        }
    }

    private void afterExecute(RedisMethodContext redisMethodContext, Object result, Throwable throwable) {
        for (int i = 0; i < interceptors.size(); i++) {
            Interceptor interceptor = interceptors.get(i);
            interceptor.after(redisMethodContext);
        }
    }
}
