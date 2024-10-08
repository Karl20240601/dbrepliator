package com.crazymaker.gateway.core.api.upstream;

import com.crazymaker.gateway.core.api.balance.BalanceStrategy;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;
import com.crazymaker.micro.core.servcie.MicroCore;

import java.util.List;

public interface ServiceManager extends MicroCore {


    List<ServiceInstance> getServiceInstances(GatewayContext context);

    BalanceStrategy getBalanceStrategy(GatewayContext context);
}
