package com.redis;

import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.resource.ClientResources;

public class AggreationRedisClusterClient extends AbstractRedisClient {
    private final TagRegistry<AbstractRedisClient> tagRegistrys;
    /**
     * Create a new instance with client resources.
     *
     * @param clientResources the client resources. If {@literal null}, the client will create a new dedicated instance of
     *                        client resources and keep track of them.
     */
    protected AggreationRedisClusterClient(TagRegistry<AbstractRedisClient> tagRegistrys,ClientResources clientResources) {
        super(clientResources);
        this.tagRegistrys = tagRegistrys;
    }
}
