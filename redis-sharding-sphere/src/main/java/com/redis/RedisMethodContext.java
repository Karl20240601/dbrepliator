package com.redis;

public class RedisMethodContext {
    public static final byte REDIS_COMMAND_READ=0;
    public static final byte REDIS_COMMAND_WRITE=1;
    private  RedisClusterModelEnum redisClusterModelEnum;
    /**
     * 命令读写
     * 0 读，1 写 3 其他
     */
    private  final byte commandType;
    private final  String key;
    private final String tag ;
    private final String NONE_TAG="" ;

    public RedisMethodContext(String key, byte commandType, String tag) {
        this.key = key;
        this.commandType = commandType;
        this.tag = tag;
    }

    public RedisMethodContext(String key, byte commandType) {
        this.key = key;
        this.commandType = commandType;
        this.tag = NONE_TAG;
    }
    public RedisClusterModelEnum getRedisClusterModelEnum() {
        return redisClusterModelEnum;
    }

    public void setRedisClusterModelEnum(RedisClusterModelEnum redisClusterModelEnum) {
        this.redisClusterModelEnum = redisClusterModelEnum;
    }

    public byte getCommandType() {
        return commandType;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "RedisMethodContext{" +
                "redisClusterModelEnum=" + redisClusterModelEnum +
                ", commandType=" + commandType +
                ", key='" + key + '\'' +
                ", tag='" + tag + '\'' +
                ", NONE_TAG='" + NONE_TAG + '\'' +
                '}';
    }
}
