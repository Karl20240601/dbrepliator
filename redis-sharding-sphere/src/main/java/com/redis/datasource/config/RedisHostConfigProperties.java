package com.redis.datasource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "sharding.shphere.redis")
public class RedisHostConfigProperties {
    private List<RedisHostConfig> hostConfigs;

    public List<RedisHostConfig> getHostConfigs() {
        return hostConfigs;
    }

    public void setHostConfigs(List<RedisHostConfig> hostConfigs) {
        this.hostConfigs = hostConfigs;
    }
}
