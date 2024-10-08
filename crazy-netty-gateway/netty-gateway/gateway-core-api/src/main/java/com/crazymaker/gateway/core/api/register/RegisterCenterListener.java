package com.crazymaker.gateway.core.api.register;

import com.crazymaker.gateway.core.api.metadata.ServiceInstance;

import java.util.Set;

public interface RegisterCenterListener {

    void onChange(ServiceDefinition serviceDefinition,
                  Set<ServiceInstance> serviceInstanceSet);
}
