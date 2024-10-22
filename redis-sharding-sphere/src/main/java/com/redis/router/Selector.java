package com.redis.router;


import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;

import java.util.List;

public interface Selector {
    TagRedisConnectionNode select(List<TagRedisConnectionNode> redisNodes, RedisMethodContext redisMethodContext);
}
