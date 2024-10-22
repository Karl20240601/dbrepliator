package com.redis.router;

import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据读写方法过滤节点
 */
public class ReadOrWrieteRouter implements Router {
    @Override
    public List<TagRedisConnectionNode> route(List<TagRedisConnectionNode> list, RedisMethodContext redisMethodContext) {
        byte commandType = redisMethodContext.getCommandType();
        List<TagRedisConnectionNode> collect = list.stream().filter(e ->e.getNodeRole().support(commandType)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE-900;
    }
}
