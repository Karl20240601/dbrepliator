package com.redis.router;

import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;

import java.util.List;
import java.util.stream.Collectors;

public class TagRouter implements Router {
    @Override
    public List<TagRedisConnectionNode> route(List<TagRedisConnectionNode> list, RedisMethodContext redisMethodContext) {
        String tag = redisMethodContext.getTag();
        List<TagRedisConnectionNode> collect = list.stream().filter(e -> e.getTag().equals(tag)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE-1000;
    }
}
