package com.redis.router;

import com.redis.RedisMethodContext;
import com.redis.TagRedisConnectionNode;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class RoundRobinLoadSelector extends AbstractSelector {

    private static final int RECYCLE_PERIOD = 60000;

    protected static class WeightedRoundRobin {
        private int weight;
        private AtomicLong current = new AtomicLong(0);
        private long lastUpdate;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
            current.set(0);
        }

        public long increaseCurrent() {
            return current.addAndGet(weight);
        }

        public void sel(int total) {
            current.addAndGet(-1 * total);
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }

    private ConcurrentMap<TagRedisConnectionNode, WeightedRoundRobin> methodWeightMap = new  ConcurrentHashMap<TagRedisConnectionNode, WeightedRoundRobin>();



    @Override
    public TagRedisConnectionNode doSelect(List<TagRedisConnectionNode> redisNodes, RedisMethodContext redisMethodContext) {
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        long now = System.currentTimeMillis();
        TagRedisConnectionNode tagRedisConnectionNode = null;
        WeightedRoundRobin selectedWRR = null;
        for (TagRedisConnectionNode node : redisNodes) {
            int weight = node.getWeight();
            WeightedRoundRobin weightedRoundRobin = methodWeightMap.computeIfAbsent(node, k -> {
                WeightedRoundRobin wrr = new WeightedRoundRobin();
                wrr.setWeight(weight);
                return wrr;
            });

            if (weight != weightedRoundRobin.getWeight()) {
                //weight changed
                weightedRoundRobin.setWeight(weight);
            }
            long cur = weightedRoundRobin.increaseCurrent();
            weightedRoundRobin.setLastUpdate(now);
            if (cur > maxCurrent) {
                maxCurrent = cur;
                tagRedisConnectionNode = node;
                selectedWRR = weightedRoundRobin;
            }
            totalWeight += weight;
        }
        if (redisNodes.size() != methodWeightMap.size()) {
            methodWeightMap.entrySet().removeIf(item -> now - item.getValue().getLastUpdate() > RECYCLE_PERIOD);
        }
        if (tagRedisConnectionNode != null) {
            selectedWRR.sel(totalWeight);
            return tagRedisConnectionNode;
        }
        // should not happen here
        return redisNodes.get(0);
    }
}
