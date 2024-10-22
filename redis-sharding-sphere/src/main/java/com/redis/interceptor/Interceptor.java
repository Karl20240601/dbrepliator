package com.redis.interceptor;

import com.redis.RedisMethodContext;

public interface Interceptor {
    public void  before(RedisMethodContext redisMethodContext);
    public void  after(RedisMethodContext redisMethodContext);

}
