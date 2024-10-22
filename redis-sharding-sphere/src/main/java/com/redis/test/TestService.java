package com.redis.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void write(String key1, String value) {
        redisTemplate.opsForValue().set(key1, value);
    }

    public String read(String key1) {
        return redisTemplate.opsForValue().get(key1);
    }
}
