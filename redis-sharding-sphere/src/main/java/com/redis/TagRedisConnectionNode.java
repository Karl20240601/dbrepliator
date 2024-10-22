package com.redis;

import com.redis.router.NodeRole;
import org.springframework.data.redis.connection.*;

import java.util.Objects;


public class TagRedisConnectionNode implements TagNode {
    private final RedisConnection delegate;

    private NodeRole nodeRole;
    private final String redisId;
    private final String tag;
    private int weight;


    /**
     *
     * @param delegate
     * @param tag
     * @param redisId
     */
    public TagRedisConnectionNode(RedisConnection delegate, String tag,String redisId) {
        this.delegate = delegate;
        this.tag = tag;
        this.redisId  = redisId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagRedisConnectionNode)) return false;
        TagRedisConnectionNode that = (TagRedisConnectionNode) o;
        return Objects.equals(getRedisId(), that.getRedisId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRedisId());
    }

    @Override
    public String getTag() {
        return tag;
    }

    public RedisConnection getRedisConnection() {
        return delegate;
    }

    public RedisConnection getDelegate() {
        return delegate;
    }

    public NodeRole getNodeRole() {
        return nodeRole;
    }

    public void setNodeRole(NodeRole nodeRole) {
        this.nodeRole = nodeRole;
    }

    public String getRedisId() {
        return redisId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
