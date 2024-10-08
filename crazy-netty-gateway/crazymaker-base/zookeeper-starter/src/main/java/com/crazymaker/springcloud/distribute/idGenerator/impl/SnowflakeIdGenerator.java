/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.crazymaker.springcloud.distribute.idGenerator.impl;

import com.crazymaker.springcloud.common.distribute.idGenerator.IdGenerator;
import com.crazymaker.springcloud.common.util.TimeUtil;
import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Calendar;
import java.util.Properties;

/**
 * Snowflake distributed primary key generator.
 *
 * <p>
 * Use snowflake algorithm. Length is 64 bit.
 * </p>
 *
 * <pre>
 * 1bit sign bit.
 * 41bits timestamp offset from 2016.11.01(ShardingSphere distributed primary key published data) to now.
 * 10bits worker process id.
 * 12bits auto increment offset in one mills
 * </pre>
 *
 * <p>
 * Call @{@code SnowflakeShardingKeyGenerator.setWorkerId} to set worker id, default value is 0.
 * </p>
 *
 * <p>
 * Call @{@code SnowflakeShardingKeyGenerator.setMaxTolerateTimeDifferenceMilliseconds} to set max tolerate time difference milliseconds, default value is 0.
 * </p>
 */
public final class SnowflakeIdGenerator implements IdGenerator {

    public static final long EPOCH;

    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_BITS = 10L;

    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

    private static final long WORKER_ID = 0;

    private static final int DEFAULT_VIBRATION_VALUE = 1;

    private static final int MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS = 10;


    @Getter
    @Setter
    private Properties properties = new Properties();

    private int sequenceOffset = -1;

    private long sequence;

    private long lastMilliseconds;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.NOVEMBER, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        EPOCH = calendar.getTimeInMillis();
    }


    private IdWorker worker;
    private String type = "undefined";
    /**
     * 该项目的worker 节点 id
     */
    private long workerId;

    private volatile boolean inited = false;


    /**
     * 最大的 worker id ，8091
     * -1 的补码（二进制全1）右移13位, 然后取反
     */
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    public static final String WORKER_PATH = "/push-center/SnowflakeId/worker-";

    public SnowflakeIdGenerator(String type) {
        this.type = type;

        worker = IdWorker.getInstance(WORKER_PATH);

    }


    /**
     * 初始化单例
     * <p>
     * workerId 节点Id,最大8091
     *
     * @return the 单例
     */
    private synchronized void init() {
        workerId = worker.getId();
        if (workerId > MAX_WORKER_ID) {
            // zk分配的workerId过大
            throw new IllegalArgumentException("woker Id wrong: " + workerId);
        }
        inited = true;
    }

    public Long nextId() {
        if (!inited) {
            init();
        }
        return generateId();
    }

    public synchronized long generateId() {
        long currentMilliseconds = TimeUtil.currentTimeMillis();
        if (waitTolerateTimeDifferenceIfNeed(currentMilliseconds)) {
            currentMilliseconds = TimeUtil.currentTimeMillis();
        }
        if (lastMilliseconds == currentMilliseconds) {
            if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)) {
                currentMilliseconds = waitUntilNextTime(currentMilliseconds);
            }
        } else {
            vibrateSequenceOffset();
            sequence = sequenceOffset;
        }
        lastMilliseconds = currentMilliseconds;
        return ((currentMilliseconds - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (getWorkerId() << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

    @SneakyThrows
    private boolean waitTolerateTimeDifferenceIfNeed(final long currentMilliseconds) {
        if (lastMilliseconds <= currentMilliseconds) {
            return false;
        }
        long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;
        Preconditions.checkState(timeDifferenceMilliseconds < getMaxTolerateTimeDifferenceMilliseconds(),
                "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", lastMilliseconds, currentMilliseconds);
        Thread.sleep(timeDifferenceMilliseconds);
        return true;
    }

    private long getWorkerId() {
        return workerId;
    }

    private int getMaxVibrationOffset() {
        int result = Integer.parseInt(properties.getProperty("max.vibration.offset", String.valueOf(DEFAULT_VIBRATION_VALUE)));
        Preconditions.checkArgument(result >= 0 && result <= SEQUENCE_MASK, "Illegal max vibration offset");
        return result;
    }

    private int getMaxTolerateTimeDifferenceMilliseconds() {
        return Integer.valueOf(properties.getProperty("max.tolerate.time.difference.milliseconds",
                String.valueOf(MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS)));
    }

    private long waitUntilNextTime(final long lastTime) {
        long result = TimeUtil.currentTimeMillis();
        while (result <= lastTime) {
            Thread.yield();
            result = TimeUtil.currentTimeMillis();
        }
        return result;
    }

    private void vibrateSequenceOffset() {
        sequenceOffset = sequenceOffset >= getMaxVibrationOffset() ? 0 : sequenceOffset + 1;
    }
}
