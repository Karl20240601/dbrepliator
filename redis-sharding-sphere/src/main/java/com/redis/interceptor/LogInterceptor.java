package com.redis.interceptor;

import com.redis.RedisMethodContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogInterceptor implements  Interceptor{
    private static Logger LOGGER = LoggerFactory.getLogger(LogInterceptor.class);
    @Override
    public void before(RedisMethodContext redisMethodContext) {
        LOGGER.info("redis execute before {} timstatmp",redisMethodContext,System.currentTimeMillis());
    }

    @Override
    public void after(RedisMethodContext redisMethodContext) {
        LOGGER.info("redis execute after {} timstatmp",redisMethodContext,System.currentTimeMillis());
    }
}
