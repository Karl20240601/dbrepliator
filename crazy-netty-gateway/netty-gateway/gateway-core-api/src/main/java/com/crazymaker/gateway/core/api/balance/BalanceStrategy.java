package com.crazymaker.gateway.core.api.balance;

import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;

import java.util.List;

/**
 * 负载均衡顶级接口
 */
public interface BalanceStrategy {

    ServiceInstance doSelect(GatewayContext context, List<ServiceInstance> instances);

    BalanceStrategy newInstance();
}
