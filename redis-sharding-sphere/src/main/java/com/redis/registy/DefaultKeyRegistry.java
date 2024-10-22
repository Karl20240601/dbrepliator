package com.redis.registy;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import io.github.resilience4j.core.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class DefaultKeyRegistry implements KeyRegistry, EnvironmentAware, InitializingBean {
    private Environment environment;
    public final String redisTagKeyMap = "redis.tagkey.mapping";
    private final Map<String, String> KEY_TAG_MAP = new HashMap<>();
    private final Map<String, String> patternMapCache = new HashMap<>();

    /**
     * 后面加上布隆过滤器校验patternMapCache key是否存在
     *
     * @param key
     * @return
     */
    @Override
    public String getTag(String key) {
        String s = KEY_TAG_MAP.get(key);
        if (StringUtils.isNotEmpty(s)) {
            return s;
        }
        //
        s = patternMapCache.get(key);
        if (StringUtils.isNotEmpty(s)) {
            return s;
        }

        Set<String> strings = KEY_TAG_MAP.keySet();
        for (String key1 : strings) {
            boolean matches = Pattern.matches(key1, key);
            if (matches) {
                String result = KEY_TAG_MAP.get(key1);
                synchronized (patternMapCache) {
                    patternMapCache.putIfAbsent(key, KEY_TAG_MAP.get(key1));
                }
                return result;
            }

        }

        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String property = environment.getProperty(redisTagKeyMap, String.class, "{}");
        Map<String, String> stringStringMap = JSONObject.parseObject(property, new TypeReference<Map<String, String>>() {
        });
        KEY_TAG_MAP.putAll(stringStringMap);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
