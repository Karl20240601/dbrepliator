package com.redis.router;

import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;

import java.util.List;

public interface RouterChain {
    void  addRouter(Router router);
    void  addRouter(List<Router> router);
    List<TagRedisConnectionNode> route(List<TagRedisConnectionNode> list, RedisMethodContext redisMethodContext);

}
