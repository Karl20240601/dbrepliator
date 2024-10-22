package com.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class AggregationRedisConnection implements DefaultedRedisConnection {
    private TagRegistry<TagRedisConnectionNode> tagRegistry;
    private boolean closed;


    public AggregationRedisConnection(TagRegistry<TagRedisConnectionNode> tagRegistry) {
        this.tagRegistry = tagRegistry;
    }



    @Override
    public void close() throws DataAccessException {
        List<TagRedisConnectionNode> tagAllNode = this.tagRegistry.getTagAllNode();
        if(CollectionUtils.isEmpty(tagAllNode)){
            return;
        }
        //异常处理
        for(TagRedisConnectionNode tagRedisConnectionNode:tagAllNode){
            tagRedisConnectionNode.getRedisConnection().close();
        }

    }

    @Override
    public boolean isClosed() {
        List<TagRedisConnectionNode> tagAllNode = this.tagRegistry.getTagAllNode();
        if(CollectionUtils.isEmpty(tagAllNode)){
            return true;
        }

        for(TagRedisConnectionNode tagRedisConnectionNode:tagAllNode){
            if(!tagRedisConnectionNode.getRedisConnection().isClosed()){
                return false;
            }
        }
        return true;
    }

    @Override
    public Object getNativeConnection() {
        return null;
    }

    @Override
    public boolean isQueueing() {
        return false;
    }

    @Override
    public boolean isPipelined() {
        return false;
    }

    @Override
    public void openPipeline() {

    }

    @Override
    public List<Object> closePipeline() throws RedisPipelineException {
        return null;
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return null;
    }


    @Override
    public Object execute(String command, byte[]... args) {
        return null;
    }

    @Override
    public void select(int dbIndex) {

    }

    @Override
    public byte[] echo(byte[] message) {
        return new byte[0];
    }

    @Override
    public String ping() {
        return null;
    }

    @Override
    public boolean isSubscribed() {
        return false;
    }

    @Override
    public Subscription getSubscription() {
        return null;
    }

    @Override
    public Long publish(byte[] channel, byte[] message) {
        return null;
    }

    @Override
    public void subscribe(MessageListener listener, byte[]... channels) {

    }

    @Override
    public void pSubscribe(MessageListener listener, byte[]... patterns) {

    }

    @Override
    public void multi() {

    }

    @Override
    public List<Object> exec() {
        return null;
    }

    @Override
    public void discard() {

    }

    @Override
    public void watch(byte[]... keys) {

    }

    @Override
    public void unwatch() {

    }
}
