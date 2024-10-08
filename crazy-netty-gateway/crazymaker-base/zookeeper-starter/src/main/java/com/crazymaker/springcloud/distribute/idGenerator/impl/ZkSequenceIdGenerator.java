package com.crazymaker.springcloud.distribute.idGenerator.impl;

import com.crazymaker.springcloud.common.distribute.idGenerator.IdGenerator;
import com.crazymaker.springcloud.common.exception.BusinessException;
import com.crazymaker.springcloud.common.mpsc.ServiceThread;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class ZkSequenceIdGenerator implements IdGenerator {

    public static final String SEQUENCE_PATH = "/push-center/sequence/";
    public static final String WORKER_PATH = "/push-center/zkId/worker-";
    private IdWorker worker;
    private String type = "undefined";

    /**
     * 该项目的worker 节点 id
     */
    private long workerId;
    private long workerIdPart;
    private long maxSeq;

    private volatile boolean inited = false;

    private static final long WORKER_ID_BITS = 8L;
    private static final long SEQUENCE_BITS = 56L;

    private volatile AtomicLong localSequence = new AtomicLong(0);
    private volatile AtomicLong delta = new AtomicLong(0);

    private RemoteIncreaseService remoteIncreaseService = new RemoteIncreaseService();
    /**
     * 最大的 worker id ，256
     * -1 的补码（二进制全1）右移13位, 然后取反
     */
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    public ZkSequenceIdGenerator(String type) {
        this.type = type;
//        worker = IdWorker.getInstance(WORKER_PATH);
        worker = IdWorker.getInstance();

    }


    /**
     * 初始化单例
     * <p>
     * workerId 节点Id,最大256
     *
     * @return the 单例
     */
    private synchronized void init() {
        workerId = worker.getId();
        if (workerId > MAX_WORKER_ID) {
            // zk分配的workerId过大
            throw new IllegalArgumentException("woker Id wrong: " + workerId);
        }

        String sequenceZnodeName = this.SEQUENCE_PATH + type + "/worker-" + workerId;
        remoteSequence = new DistributedAtomicLong(worker.getClient(), sequenceZnodeName, retryPolicy);

        // 做一下校验，把数据库里边的最大值取出来，max，和remoteSequence值做比对

        localSequence.set(getRemoteValue());

        //workerId 左移
        this.workerIdPart = (this.workerId + 1) << SEQUENCE_BITS;
        maxSeq = ~(-1L << SEQUENCE_BITS);
        log.info("worker id is {},worker id part is {}", workerId, workerIdPart);
        remoteIncreaseService.start();
        inited = true;
    }

    public Long nextId() {
        if (!inited) {
            init();
        }
        return generateId();
    }


    private Long generateId() {
        long sequence = localSequence.incrementAndGet();

        if (sequence > maxSeq) {
            throw BusinessException.builder().errMsg("id 生产出错，太大了,>" + maxSeq).build();

        }
        delta.incrementAndGet();
        remoteIncreaseService.wakeup();
        return workerIdPart | sequence;
    }

    RetryPolicy retryPolicy = new RetryNTimes(10, 100);

    DistributedAtomicLong remoteSequence;


    /**
     * 获取值
     *
     * @return
     */
    public Long getRemoteValue() {
        AtomicValue<Long> sequence = null;
        try {
            sequence = this.remoteSequence.get();

            if (sequence.succeeded()) {
                Long val = sequence.postValue();
                log.debug("生产id 上一次的值：{}", val);
                return val;
            } else {
                throw BusinessException.builder().errMsg("id 生产出错").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw BusinessException.builder().errMsg("id 生产出错").build();

    }


    /**
     * 增加值
     *
     * @return
     */
    public void incrementRemote() {
        while (true) {
            long toAdd = delta.get();
            if (toAdd <= 0) {
                break;
            }
            AtomicValue<Long> sequence = null;
            try {
                sequence = this.remoteSequence.add(toAdd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (sequence.succeeded()) {
                log.debug("生产id 的seq：{}", sequence.postValue());
                delta.addAndGet(0L - toAdd);
                break;
            } else {
                log.error("生产id 失败,继续重试：{}", type);
            }

        }
    }


    class RemoteIncreaseService extends ServiceThread {


        @Override
        public void run() {
            log.info(this.getServiceName() + " service started");

            while (!this.isStopped()) {
                this.waitForRunning(1000);
                ZkSequenceIdGenerator.this.incrementRemote();
            }

            log.info(this.getServiceName() + " service end");
        }

        @Override
        public String getServiceName() {
            return "RemoteIncreaseService:" + type;
        }
    }

}
