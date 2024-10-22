package com.redis.router;

import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;

import java.util.List;

public interface Router extends Ordered{
     List<TagRedisConnectionNode> route(List<TagRedisConnectionNode> list, RedisMethodContext redisMethodContext);
}
