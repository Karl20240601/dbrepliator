package com.crazymaker.gateway.core.api.register;

import com.crazymaker.gateway.core.api.context.GatewayRequest;
import com.crazymaker.gateway.core.api.metadata.Rule;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;
import com.crazymaker.micro.core.servcie.MicroCore;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface RegisterCenter extends MicroCore {


    void register(ServiceDefinition serviceDefinition, ServiceInstance serviceInstance);

    void deregister(ServiceDefinition serviceDefinition, ServiceInstance serviceInstance);

    void subscribeAllServices(RegisterCenterListener registerCenterListener);

    /**
     *   初始化
     * @param registerAddress
     * @param env
     */
    void init(String registerAddress, String env);

}
