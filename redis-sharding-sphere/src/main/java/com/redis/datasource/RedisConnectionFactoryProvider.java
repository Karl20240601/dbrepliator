package com.redis.datasource;

import com.redis.TagRedisConnectionNode;
import com.redis.datasource.config.RedisHostConfigProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.net.UnknownHostException;
import java.util.List;

public interface RedisConnectionFactoryProvider {
      public List<TagRedisConnectionNode> createTagRedisConnectionNode(RedisHostConfigProperties redisHostConfigProperties, RedisProperties redisProperties) throws UnknownHostException;
}
