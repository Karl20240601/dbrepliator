package com.redis.support;

import io.lettuce.core.api.StatefulConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionProvider;

import java.util.concurrent.CompletionStage;

public class AggregationLettuceConnectionProvider implements LettuceConnectionProvider {

    @Override
    public <T extends StatefulConnection<?, ?>> CompletionStage<T> getConnectionAsync(Class<T> connectionType) {
        return null;
    }
}
