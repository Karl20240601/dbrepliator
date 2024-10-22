package com.redis.datasource;


import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;

import java.util.List;

public class NonFunctionalRedisConnection implements  DefaultedRedisConnection{

    @Override
    public void close() throws DataAccessException {

    }

    @Override
    public boolean isClosed() {
        return false;
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
