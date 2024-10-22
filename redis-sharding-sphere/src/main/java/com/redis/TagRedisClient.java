package com.redis;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.resource.ClientResources;

public class TagRedisClient implements TagNode {

    @Override
    public String getTag() {
        return null;
    }

    private final String tag;

    private final AbstractRedisClient redisClient;

    public TagRedisClient(String tag, AbstractRedisClient redisClient) {
        this.tag = tag;
        this.redisClient = redisClient;
    }

    public AbstractRedisClient getRedisClient() {
        return redisClient;
    }
}
