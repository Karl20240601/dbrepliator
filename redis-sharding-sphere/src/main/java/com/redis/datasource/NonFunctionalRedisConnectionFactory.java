package com.redis.datasource;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.*;

import java.io.IOException;
import java.util.Collection;

public class NonFunctionalRedisConnectionFactory implements RedisConnectionFactory {

    @Override
    public RedisConnection getConnection() {
        return new NonFunctionalRedisConnection();
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return new NonFunctionalRedisClusterConnection();

    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return false;
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return new RedisSentinelConnection(){
            @Override
            public void failover(NamedNode master) {

            }

            @Override
            public Collection<RedisServer> masters() {
                return null;
            }

            @Override
            public Collection<RedisServer> slaves(NamedNode master) {
                return null;
            }

            @Override
            public void remove(NamedNode master) {

            }

            @Override
            public void monitor(RedisServer master) {

            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public boolean isOpen() {
                return false;
            }
        };
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException e) {
        return null;
    }
}
