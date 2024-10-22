package com.redis.metadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.util.ReflectionUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

import static com.redis.utils.RedisCommandsUtils.*;
import static java.util.Collections.unmodifiableMap;
import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

/**
 * Redis Metadata Repository
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
public class RedisMetadataRepository {

    private static final Logger logger = LoggerFactory.getLogger(RedisMetadataRepository.class);


    private static   final Map<String, MethodMetadata> methodMetadataMap = initMethodMetadataCache();

    static final Map<String, Function<RedisConnection, Object>> redisCommandBindings = initRedisCommandBindings();



    private static Map<String, MethodMetadata> initMethodMetadataCache() {
        RedisMetadata redisMetadata = loadRedisMetadata();
        List<MethodMetadata> methodMetadataList = redisMetadata.getMethods();
        int size = methodMetadataList.size();
        Map<String, MethodMetadata> redisMetadataCache = new HashMap<>(size * 2);
        for (int i = 0; i < size; i++) {
            MethodMetadata methodMetadata = methodMetadataList.get(i);
            String key = buildCommandMethodId(methodMetadata.getInterfaceName(), methodMetadata.getMethodName(), methodMetadata.getParameterTypes());
            redisMetadataCache.put(key,methodMetadata);
        }
        return unmodifiableMap(redisMetadataCache);
    }



    private static RedisMetadata loadRedisMetadata() {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        RedisMetadata redisMetadata = new RedisMetadata();
        try {
            Resource[] resources = resolver.getResources(CLASSPATH_ALL_URL_PREFIX + "/META-INF/redis-metadata.yaml");
            int size = resources.length;
            for (int i = 0; i < size; i++) {
                Resource resource = resources[i];
                redisMetadata.merge(loadRedisMetadata(resource));
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return redisMetadata;
    }

    private static RedisMetadata loadRedisMetadata(Resource resource) throws IOException {
        Yaml yaml = new Yaml();
        RedisMetadata redisMetadata = yaml.loadAs(resource.getInputStream(), RedisMetadata.class);
        return redisMetadata;
    }

    public static MethodMetadata findMethodMetadata(Method method){
        String key = buildCommandMethodId(method);
        return methodMetadataMap.get(key);

    }


    private static Map<String, Function<RedisConnection, Object>> initRedisCommandBindings() {
        Class<?> redisCommandInterfaceClass = RedisConnection.class;
        Method[] redisCommandMethods = redisCommandInterfaceClass.getMethods();
        int length = redisCommandMethods.length;
        Map<String, Function<RedisConnection, Object>> redisCommandBindings = new HashMap<>(1);
        for (int i = 0; i < length; i++) {
            Method redisCommandMethod = redisCommandMethods[i];
            initRedisCommandBindings(redisCommandInterfaceClass, redisCommandMethod, redisCommandBindings);
        }
        return unmodifiableMap(redisCommandBindings);
    }

    private static void initRedisCommandBindings(Class<?> redisCommandInterfaceClass, Method redisCommandMethod, Map<String, Function<RedisConnection, Object>> redisCommandBindings) {
        Class<?> returnType = redisCommandMethod.getReturnType();
        //if (returnType.isAssignableFrom(redisCommandInterfaceClass) && redisCommandMethod.getParameterCount() < 1) {
        if (returnType.equals(redisCommandInterfaceClass) && redisCommandMethod.getParameterCount() < 1) {
            String interfaceName = returnType.getName();
            redisCommandBindings.put(interfaceName, redisConnection -> ReflectionUtils.invokeMethod(redisCommandMethod, redisConnection));
            logger.debug("Redis command interface {} Bind RedisConnection command object method {}", interfaceName, redisCommandMethod);
        }
    }

    public static Function<RedisConnection, Object> getRedisCommandBindingFunction(String interfaceName) {
        return redisCommandBindings.getOrDefault(interfaceName, redisConnection -> redisConnection);
    }
}
