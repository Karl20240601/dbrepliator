package com.crazymaker.springcloud.distribute.idGenerator.impl;

import com.crazymaker.springcloud.distribute.zookeeper.ZKClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * build by 尼恩 @ 疯狂创客圈
 **/
@Data
@Slf4j
public class IdWorker {


    //Zk客户端
    private CuratorFramework client = null;

    //工作节点的路径
    private String pathPrefix = "/push-center/IDMaker/worker-";
    public static final String DEFAUTL_PATH_PREFIX = "/push-center/name-service/worker-";
    private String pathRegistered = null;
    private volatile boolean inited = false;
    public static final Map<String, IdWorker> cache = new ConcurrentHashMap<>();
    private long workId;


    private IdWorker(String pathPrefix) {
        this.pathPrefix = pathPrefix;

    }

    public static synchronized IdWorker getInstance() {


        return getInstance(DEFAUTL_PATH_PREFIX);
    }

    public static synchronized IdWorker getInstance(String workerPath) {

        if (cache.containsKey(workerPath)) {
            return cache.get(workerPath);
        }

        IdWorker worker = new IdWorker(workerPath);
        cache.put(workerPath, worker);
        return worker;
    }


    // 在zookeeper中创建临时节点并写入信息
    public synchronized void init() {
        client = ZKClient.getSingleton().getClient();

        ZKClient.getSingleton().deleteWhenHasNoChildren(pathPrefix);

        createZNode();

        String sid = null;
        if (null == pathRegistered) {
            throw new RuntimeException("节点注册失败:" + pathPrefix);
        }
        int index = pathRegistered.lastIndexOf(pathPrefix);
        if (index >= 0) {
            index += pathPrefix.length();
            sid = index <= pathRegistered.length() ? pathRegistered.substring(index) : null;
        }

        if (null == sid) {
            throw new RuntimeException("节点ID生成失败");
        }

        workId = Long.parseLong(sid);

        log.info("the worker of type{} id  is {}", pathPrefix, workId);
        inited = true;
    }


    private void createZNode() {
        // 创建一个 ZNode 节点
        // 节点的 payload 为当前worker 实例
        pathRegistered = ZKClient.getSingleton().createEphemeralSeqNode(pathPrefix);
//        try {
////            byte[] payload = JsonUtil.object2JsonBytes(this);
//
//            pathRegistered = client.create()
//                    .creatingParentsIfNeeded()
//                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
//                    .forPath(pathPrefix);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public long getId() {
        if (!inited) {
            init();
        }
        return workId;

    }
}