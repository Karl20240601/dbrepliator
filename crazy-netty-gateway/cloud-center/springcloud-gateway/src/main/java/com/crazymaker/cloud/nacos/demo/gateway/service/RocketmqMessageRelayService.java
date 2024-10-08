package com.crazymaker.cloud.nacos.demo.gateway.service;

import com.crazymaker.springcloud.common.constants.SessionConstants;
import com.crazymaker.springcloud.common.distribute.idGenerator.IdFactory;
import com.crazymaker.springcloud.common.distribute.idGenerator.IdGenerator;
import com.crazymaker.springcloud.common.util.ThreadUtil;
import com.crazymaker.springcloud.distribute.idGenerator.impl.IdWorker;
import com.crazymaker.springcloud.distribute.idGenerator.impl.ZkSequenceIdGenerator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class RocketmqMessageRelayService implements ApplicationContextAware {
    public static final int SIZE_LIMIT = 1024 * 1024 * 4;

    @Resource
    IdFactory idFactory;
    IdGenerator msgIdWrapper;

    @Value("${rocketmq.nameServer}")
    private String rocketmqAddress;
    @Value("${rocketmq.producer.relayGroup}")
    private String producerRelayGroup;


    @Value("${rocketmq.producer.relayTopic}")
    private String relayTopic;

    @Value("${rocketmq.producer.concurrent}")
    private int concurrent = 1;

    @Value("${rocketmq.producer.retryTimes}")
    private int retryTimes = 1;
    @Value("${rocketmq.producer.maxWaited}")
    private int maxWaited = 100000;


    private long workerId;

    List<InnerSender> senders = null;
    LinkedBlockingQueue<Message> messagesBlockList = new LinkedBlockingQueue<>();
    ZkSequenceIdGenerator msgIdGenerator = null;


    Executor executor = null;
    private AtomicInteger producerInUseCount = new AtomicInteger(0);
    private volatile boolean inited = false;

    public Long putMessage(byte[] messageBody, String userId) {

        if (messagesBlockList.size() > maxWaited) {
            log.error("缓存超出阈值，请稍后再发");
            return null;
        }


//        Long id = msgIdGenerator.nextId();
        Long id = getMsgIdGenerator().nextId();

        Message message = new Message(relayTopic /* Topic */,
                "toReplace" /* Tag */,
                String.valueOf(id),
                messageBody);

        if (StringUtils.isNotEmpty(userId)) {
            message.putUserProperty(SessionConstants.USER_IDENTIFIER, userId);
        }


        messagesBlockList.add(message);
        int index = producerInUseCount.get();
        if (index >= concurrent) {
            return id;
        }


        producerInUseCount.incrementAndGet();
        executor.execute(new Runnable() {
            @Override
            public void run() {

                doSend(index);
                producerInUseCount.decrementAndGet();
            }
        });
        return id;
    }

    private void doSend(int index) {

        if (index < 0) index = 0;

        InnerSender sender = senders.get(index);
        Message message = null;
        boolean waitToLong = false;
        List<Message> batchList = new LinkedList<>();
        int totalSize = 0;

        //死循环
        while (!waitToLong) {

            try {
                message = messagesBlockList.poll(200, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                waitToLong = true;
            }


            if (waitToLong || null == message) {
                if (batchList.size() > 0) {
                    sender.syncSend(batchList);
                    log.info("批量发送，一次发送{} 条消息", batchList.size());
                    batchList.clear();
                    batchList = null;
                }
                break;
            }


            //topic +body
            int currentSize = sender.relayTopicLength + message.getBody().length;

            // peperties

            Map<String, String> properties = message.getProperties();
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                currentSize += entry.getKey().length() + entry.getValue().length();
            }

            // 增加rocketmq日志的开销20字节
            currentSize = currentSize + 20;
            if (currentSize > SIZE_LIMIT) {
                //单个消息超过了最大的限制
                //忽略,否则会阻塞分裂的进程
                log.error("消息太大");
//                sender.asynSend(message);
                continue;
            }

            if (batchList.size() > 0 && currentSize + totalSize > SIZE_LIMIT) {
                //发送批量消息
                sender.syncSend(batchList);
                batchList.clear();
                totalSize = 0;
            }

            batchList.add(message);
            totalSize += currentSize;


        }
    }

    @Data
    static class InnerSender {

        private String relayTopic;
        private String rocketmqAddress;
        private String producerGroup;
        private String topic;
        private int relayTopicLength;
        private String instanceName;


        DefaultMQProducer mqProducer = null;
        private int retryTimes = 3;

        public void asynSend(Message msg) {
            try {     // SendCallback接收异步返回结果的回调
                mqProducer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("send ok,instance： {} ,message id ：{}", instanceName, sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        log.error("send error,instance： {} ,error is ：{}", instanceName, e.getMessage());
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void syncSend(Collection<Message> msgs) {
            try {
                // SendCallback接收异步返回结果的回调
                mqProducer.send(msgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void startMqProducer() {
            mqProducer = new DefaultMQProducer(producerGroup);
            //指定NameServer地址
            mqProducer.setNamesrvAddr(rocketmqAddress); //修改为自己的
            mqProducer.setRetryTimesWhenSendFailed(retryTimes);
            mqProducer.setInstanceName(instanceName);
            mqProducer.setProducerGroup(producerGroup);
            /**
             * Producer对象在使用之前必须要调用start初始化，初始化一次即可
             * 注意：切记不可以在每次发送消息时，都调用start方法
             */
            try {
                mqProducer.start();
                log.info("product {} start ...", instanceName);
            } catch (MQClientException e) {
                e.printStackTrace();
            }
        }


    }


    private synchronized void startMutiSender() {

        if (inited) return;
        inited = true;

        executor = new ThreadPoolExecutor(
                concurrent,
                concurrent,
                ThreadUtil.KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(ThreadUtil.QUEUE_SIZE),
                new ThreadUtil.CustomThreadFactory("MqProducer"));
        ;


        senders = new ArrayList<>(concurrent);
        for (int i = 0; i < concurrent; i++) {
            InnerSender sender = new InnerSender();
            senders.add(sender);
            sender.setInstanceName(producerRelayGroup + "-" + workerId + "-" + i);
            sender.setProducerGroup(producerRelayGroup);
            sender.setRocketmqAddress(rocketmqAddress);
            sender.setRetryTimes(retryTimes);
            sender.setRelayTopic(relayTopic);
            sender.setRelayTopicLength(sender.getRelayTopic().getBytes().length);

            sender.startMqProducer();

        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        IdWorker idWorker = IdWorker.getInstance();
        workerId = idWorker.getWorkId();

        msgIdGenerator = new ZkSequenceIdGenerator("MsgPO");
        startMutiSender();
    }

    public IdGenerator getMsgIdGenerator() {
        if (null == msgIdWrapper) {
            msgIdWrapper = idFactory.getIdGenerator("test");
        }
        return msgIdWrapper;
    }
}
