package com.crazymaker.springcloud.standard.config;

import com.crazymaker.springcloud.common.distribute.idGenerator.IdGenerator;
import com.crazymaker.springcloud.common.distribute.rateLimit.RateLimitService;
import com.crazymaker.springcloud.distribute.idGenerator.impl.SnowflakeIdGenerator;
import com.crazymaker.springcloud.distribute.idGenerator.impl.ZkSequenceIdGenerator;
import com.crazymaker.springcloud.distribute.lock.LockService;
import com.crazymaker.springcloud.distribute.zookeeper.ZKClient;
import com.crazymaker.springcloud.standard.lock.impl.ZkLockServiceImpl;
import com.crazymaker.springcloud.standard.rateLimit.impl.ZkRateLimitServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@ConditionalOnProperty(prefix = "zookeeper", name = "address")
public class CustomedZookeeperAutoConfiguration {
    @Value("${zookeeper.address}")
    private String zkAddress;

    /**
     * 自定义的ZK客户端bean
     *
     * @return
     */
//    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean(name = "zKClient")
    public ZKClient zKClient() {
        return new ZKClient(zkAddress);
    }

    /**
     * 获取 ZK 限流器的 bean
     */
    @Bean
    @DependsOn("zKClient")
    public RateLimitService zkRateLimitServiceImpl() {
        return new ZkRateLimitServiceImpl();
    }

    /**
     * 获取 ZK 分布式锁的 bean
     */

    @Bean
    @DependsOn("zKClient")
    public LockService zkLockServiceImpl() {
        return new ZkLockServiceImpl();
    }


    /**
     * 获取 ZkSequenceIdGenerator 分布式ID 生成器
     */
    @Bean
    @DependsOn("zKClient")
    public IdGenerator zkSequenceIdGenerator() {
        return new ZkSequenceIdGenerator("demo");
    }


    /**
     * 获取SnowflakeIdGenerator分布式ID 生成器
     */
    @Bean
    @DependsOn("zKClient")
    public IdGenerator snowflakeIdGenerator() {
        return new SnowflakeIdGenerator("demo");
    }


}
