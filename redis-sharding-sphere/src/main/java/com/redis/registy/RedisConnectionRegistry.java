package com.redis.registy;

import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;


public interface RedisConnectionRegistry {
    TagRedisConnectionNode select(RedisMethodContext redisMethodContext);
    void register(TagRedisConnectionNode tagRedisConnectionNode);
}
