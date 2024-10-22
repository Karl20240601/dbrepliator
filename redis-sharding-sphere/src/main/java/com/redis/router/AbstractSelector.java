package com.redis.router;


import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;
import org.springframework.util.CollectionUtils;

import java.util.List;

public abstract class AbstractSelector implements Selector {
    public TagRedisConnectionNode select(List<TagRedisConnectionNode> redisNodes, RedisMethodContext redisMethodContext){
        if(CollectionUtils.isEmpty(redisNodes)){
            return null;
        }

        if(redisNodes.size()==1){
            return redisNodes.get(0);
        }

        return doSelect(redisNodes, redisMethodContext);
    }

    public abstract TagRedisConnectionNode doSelect(List<TagRedisConnectionNode> redisNodes, RedisMethodContext redisMethodContext);
}
