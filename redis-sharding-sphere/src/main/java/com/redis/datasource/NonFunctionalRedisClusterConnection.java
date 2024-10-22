package com.redis.datasource;


import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.RedisClientInfo;

import java.util.*;

public class NonFunctionalRedisClusterConnection implements  DefaultedRedisConnection,RedisClusterConnection{

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

    @Override
    public String ping(RedisClusterNode node) {
        return null;
    }

    @Override
    public Set<byte[]> keys(RedisClusterNode node, byte[] pattern) {
        return null;
    }

    @Override
    public Cursor<byte[]> scan(RedisClusterNode node, ScanOptions options) {
        return null;
    }

    @Override
    public byte[] randomKey(RedisClusterNode node) {
        return new byte[0];
    }

    @Override
    public <T> T execute(String command, byte[] key, Collection<byte[]> args) {
        return null;
    }

    @Override
    public Iterable<RedisClusterNode> clusterGetNodes() {
        return null;
    }

    @Override
    public Collection<RedisClusterNode> clusterGetSlaves(RedisClusterNode master) {
        return null;
    }

    @Override
    public Map<RedisClusterNode, Collection<RedisClusterNode>> clusterGetMasterSlaveMap() {
        return null;
    }

    @Override
    public Integer clusterGetSlotForKey(byte[] key) {
        return null;
    }

    @Override
    public RedisClusterNode clusterGetNodeForSlot(int slot) {
        return null;
    }

    @Override
    public RedisClusterNode clusterGetNodeForKey(byte[] key) {
        return null;
    }

    @Override
    public ClusterInfo clusterGetClusterInfo() {
        return null;
    }

    @Override
    public void clusterAddSlots(RedisClusterNode node, int... slots) {

    }

    @Override
    public void clusterAddSlots(RedisClusterNode node, RedisClusterNode.SlotRange range) {

    }

    @Override
    public Long clusterCountKeysInSlot(int slot) {
        return null;
    }

    @Override
    public void clusterDeleteSlots(RedisClusterNode node, int... slots) {

    }

    @Override
    public void clusterDeleteSlotsInRange(RedisClusterNode node, RedisClusterNode.SlotRange range) {

    }

    @Override
    public void clusterForget(RedisClusterNode node) {

    }

    @Override
    public void clusterMeet(RedisClusterNode node) {

    }

    @Override
    public void clusterSetSlot(RedisClusterNode node, int slot, AddSlots mode) {

    }

    @Override
    public List<byte[]> clusterGetKeysInSlot(int slot, Integer count) {
        return null;
    }

    @Override
    public void clusterReplicate(RedisClusterNode master, RedisClusterNode replica) {

    }

    @Override
    public void bgReWriteAof(RedisClusterNode node) {

    }

    @Override
    public void bgSave(RedisClusterNode node) {

    }

    @Override
    public Long lastSave(RedisClusterNode node) {
        return null;
    }

    @Override
    public void save(RedisClusterNode node) {

    }

    @Override
    public Long dbSize(RedisClusterNode node) {
        return null;
    }

    @Override
    public void flushDb(RedisClusterNode node) {

    }

    @Override
    public void flushAll(RedisClusterNode node) {

    }

    @Override
    public Properties info(RedisClusterNode node) {
        return null;
    }

    @Override
    public Properties info(RedisClusterNode node, String section) {
        return null;
    }

    @Override
    public void shutdown(RedisClusterNode node) {

    }

    @Override
    public Properties getConfig(RedisClusterNode node, String pattern) {
        return null;
    }

    @Override
    public void setConfig(RedisClusterNode node, String param, String value) {

    }

    @Override
    public void resetConfigStats(RedisClusterNode node) {

    }

    @Override
    public Long time(RedisClusterNode node) {
        return null;
    }

    @Override
    public List<RedisClientInfo> getClientList(RedisClusterNode node) {
        return null;
    }
}
