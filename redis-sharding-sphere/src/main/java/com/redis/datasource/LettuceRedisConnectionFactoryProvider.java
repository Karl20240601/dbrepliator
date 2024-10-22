package com.redis.datasource;

import com.redis.TagRedisConnectionNode;
import com.redis.datasource.config.RedisHostConfig;
import com.redis.datasource.config.RedisHostConfigProperties;
import com.redis.router.NodeRole;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 参考 LettuceConnectionConfiguration
 */
public class LettuceRedisConnectionFactoryProvider implements RedisConnectionFactoryProvider {
    private final List<RedisConnectionFactory> redisConnectionFactoryList = new ArrayList<>();
    private final List<TagRedisConnectionNode> redisTagRedisConnectionNodeList = new ArrayList<>();

    public RedisConnectionFactory createRedisConnectionFactory(RedisHostConfig hostConfig, RedisProperties redisProperties) throws UnknownHostException {
        RedisProperties redisProperties1 = new RedisProperties();
        BeanUtils.copyProperties(redisProperties, redisProperties1);
        if (hostConfig.isStandaloan()) {
            URI uri = parseHostInfo(hostConfig.getHosts());
            redisProperties1.setPort(uri.getPort());
            redisProperties1.setHost(uri.getHost());
            redisProperties1.setSsl(hostConfig.getHosts().startsWith("rediss://"));
        }
        if (hostConfig.isRedisCluster()) {
            redisProperties1.getCluster().setNodes(Arrays.asList(hostConfig.getHosts().split(",")));
            redisProperties1.setSsl(hostConfig.getHosts().startsWith("rediss://"));
        }
        LettuceConnectionFactory factory = createRedisConnectionFactory(DefaultClientResources.create(), redisProperties1);
        factory.afterPropertiesSet();
        return factory;
    }

    private LettuceConnectionFactory createRedisConnectionFactory(
            ClientResources clientResources, RedisProperties redisProperties) throws UnknownHostException {
        LettuceClientConfiguration clientConfig = getLettuceClientConfiguration(clientResources, redisProperties);
        return createLettuceConnectionFactory(redisProperties, clientConfig);
    }


    private LettuceClientConfiguration getLettuceClientConfiguration(
//            ObjectProvider<LettuceClientConfigurationBuilderCustomizer> builderCustomizers,
            ClientResources clientResources, RedisProperties redisProperties) {
        LettuceClientConfiguration.LettuceClientConfigurationBuilder builder = createBuilder(redisProperties.getLettuce().getPool());
        applyProperties(builder, redisProperties);
        if (redisProperties.isSsl()) {
            builder.useSsl();
        }
        builder.clientResources(clientResources);
//        builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder.build();
    }

    private LettuceClientConfiguration.LettuceClientConfigurationBuilder applyProperties(
            LettuceClientConfiguration.LettuceClientConfigurationBuilder builder, RedisProperties redisProperties) {
        if (redisProperties.isSsl()) {
            builder.useSsl();
        }
        if (redisProperties.getTimeout() != null) {
            builder.commandTimeout(redisProperties.getTimeout());
        }
        if (redisProperties.getLettuce() != null) {
            RedisProperties.Lettuce lettuce = redisProperties.getLettuce();
            if (lettuce.getShutdownTimeout() != null && !lettuce.getShutdownTimeout().isZero()) {
                builder.shutdownTimeout(redisProperties.getLettuce().getShutdownTimeout());
            }
        }
        if (StringUtils.hasText(redisProperties.getClientName())) {
            builder.clientName(redisProperties.getClientName());
        }
        return builder;
    }


    private LettuceClientConfiguration.LettuceClientConfigurationBuilder createBuilder(RedisProperties.Pool pool) {
        if (pool == null) {
            return LettuceClientConfiguration.builder();
        }
        return LettucePoolingClientConfiguration.builder().poolConfig(getPoolConfig(pool));
    }

    private GenericObjectPoolConfig<?> getPoolConfig(RedisProperties.Pool properties) {
        GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMinIdle(properties.getMinIdle());
        if (properties.getTimeBetweenEvictionRuns() != null) {
            config.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRuns().toMillis());
        }
        if (properties.getMaxWait() != null) {
            config.setMaxWaitMillis(properties.getMaxWait().toMillis());
        }
        return config;
    }


    protected final RedisSentinelConfiguration getSentinelConfig(RedisProperties redisProperties) {

        RedisProperties.Sentinel sentinelProperties = redisProperties.getSentinel();
        if (sentinelProperties != null) {
            RedisSentinelConfiguration config = new RedisSentinelConfiguration();
            config.master(sentinelProperties.getMaster());
            config.setSentinels(createSentinels(sentinelProperties));
            if (redisProperties.getPassword() != null) {
                config.setPassword(RedisPassword.of(redisProperties.getPassword()));
            }
            config.setDatabase(redisProperties.getDatabase());
            return config;
        }
        return null;
    }

    private List<RedisNode> createSentinels(RedisProperties.Sentinel sentinel) {
        List<RedisNode> nodes = new ArrayList<>();
        for (String node : sentinel.getNodes()) {
            try {
                String[] parts = StringUtils.split(node, ":");
                Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
            } catch (RuntimeException ex) {
                throw new IllegalStateException("Invalid redis sentinel property '" + node + "'", ex);
            }
        }
        return nodes;
    }

    private LettuceConnectionFactory createLettuceConnectionFactory(RedisProperties redisProperties, LettuceClientConfiguration clientConfiguration) {
        if (getSentinelConfig(redisProperties) != null) {
            return new LettuceConnectionFactory(getSentinelConfig(redisProperties), clientConfiguration);
        }
        if (getClusterConfiguration(redisProperties) != null) {
            return new LettuceConnectionFactory(getClusterConfiguration(redisProperties), clientConfiguration);
        }
        return new LettuceConnectionFactory(getStandaloneConfig(redisProperties), clientConfiguration);
    }


    protected final RedisClusterConfiguration getClusterConfiguration(RedisProperties redisProperties) {
        if (redisProperties.getCluster() == null) {
            return null;
        }
        RedisProperties.Cluster clusterProperties = redisProperties.getCluster();
        RedisClusterConfiguration config = new RedisClusterConfiguration(clusterProperties.getNodes());
        if (clusterProperties.getMaxRedirects() != null) {
            config.setMaxRedirects(clusterProperties.getMaxRedirects());
        }
        if (redisProperties.getPassword() != null) {
            config.setPassword(RedisPassword.of(redisProperties.getPassword()));
        }
        return config;
    }


    protected final RedisStandaloneConfiguration getStandaloneConfig(RedisProperties redisProperties) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());
        config.setPassword(RedisPassword.of(redisProperties.getPassword()));
        config.setDatabase(redisProperties.getDatabase());
        return config;
    }


    private URI parseHostInfo(String url) {
        try {
            String localUrl = url;
            if(!url.startsWith("rediss://")){
                localUrl = String.format("redis://%s",url);
            }
            URI uri = new URI(localUrl);
            return uri;
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Malformed url '" + url + "'", ex);
        }
    }

    @Override
    public List<TagRedisConnectionNode> createTagRedisConnectionNode(RedisHostConfigProperties redisHostConfigProperties, RedisProperties redisProperties) throws UnknownHostException {
        List<RedisHostConfig> hostConfigs = redisHostConfigProperties.getHostConfigs();
        if (CollectionUtils.isEmpty(hostConfigs)) {
            throw new RuntimeException("没有redis相关配置信息");
        }
        ArrayList<TagRedisConnectionNode> objects = new ArrayList<>(hostConfigs.size());
        for (RedisHostConfig hostConfig : hostConfigs) {
            RedisConnectionFactory redisConnectionFactory = createRedisConnectionFactory(hostConfig, redisProperties);
            redisConnectionFactoryList.add(redisConnectionFactory);
            TagRedisConnectionNode tagRedisConnectionNode = createTagRedisConnectionNode(hostConfig, redisConnectionFactory);
            redisTagRedisConnectionNodeList.add(tagRedisConnectionNode);
        }
        return Collections.unmodifiableList(redisTagRedisConnectionNodeList);
    }

    private TagRedisConnectionNode createTagRedisConnectionNode(RedisHostConfig redisHostConfig, RedisConnectionFactory redisConnectionFactory) {
        TagRedisConnectionNode tagRedisConnectionNode = null;
        NodeRole nodeRole = NodeRole.valueOf(redisHostConfig.getRole().toUpperCase());
        if (RedisHostConfig.CLUSTER_TYPE_CLUSTER.equals(redisHostConfig.getClusterType())) {
            tagRedisConnectionNode = new TagRedisConnectionNode(redisConnectionFactory.getClusterConnection(), redisHostConfig.getTag(), redisHostConfig.getHosts());
        } else {
            tagRedisConnectionNode = new TagRedisConnectionNode(redisConnectionFactory.getConnection(), redisHostConfig.getTag(), redisHostConfig.getHosts());
        }
        tagRedisConnectionNode.setNodeRole(nodeRole);
        return tagRedisConnectionNode;
    }
}
