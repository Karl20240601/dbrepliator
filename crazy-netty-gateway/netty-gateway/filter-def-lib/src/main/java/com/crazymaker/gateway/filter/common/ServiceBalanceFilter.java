package com.crazymaker.gateway.filter.common;

import com.crazymaker.gateway.core.api.balance.BalanceStrategy;
import com.crazymaker.gateway.core.api.config.GlobalSingletons;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.error.ResponseException;
import com.crazymaker.gateway.core.api.filterchain.DefaultLinkedFilter;
import com.crazymaker.gateway.core.api.filterchain.FilterAnnotation;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;
import com.crazymaker.gateway.core.api.upstream.ServiceManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.crazymaker.gateway.core.api.constant.BasicConst.ROUTER_FILTER_ORDER;
import static com.crazymaker.gateway.core.api.constant.ContextConst.SERVICE_INSTANCES;
import static com.crazymaker.gateway.core.api.constant.ContextConst.SERVICE_IN_USE;
import static com.crazymaker.gateway.core.api.constant.ResponseCode.SERVICE_UNAVAILABLE;
import static com.crazymaker.gateway.core.api.filterchain.ProcessorFilterType.PRE;

/**
 * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 * @一句话介绍： http 异步路由过滤器
 * @date
 * @详细介绍： http 异步路由过滤器
 */
@Slf4j
@FilterAnnotation(id = "ServiceBalanceFilter",
        name = "",
        type = PRE,
        order = ROUTER_FILTER_ORDER - 100)
public class ServiceBalanceFilter extends DefaultLinkedFilter<GatewayContext> {

    public void doFilter(GatewayContext context, Object... args) throws Throwable {
        ServiceManager serviceManager = GlobalSingletons.getServiceManager();
        List<ServiceInstance> serviceInstances = context.attr(SERVICE_INSTANCES);
        if (serviceInstances == null) {
            serviceInstances = serviceManager.getServiceInstances(context);
            context.setAttr(SERVICE_INSTANCES, serviceInstances);
        }
        if (serviceInstances.size() == 0) {
            //异常
            throw new ResponseException(SERVICE_UNAVAILABLE);
        }
        if (serviceInstances.size() == 1) {
            context.setAttr(SERVICE_IN_USE, serviceInstances.get(0));
        }
        BalanceStrategy balanceStrategy = serviceManager.getBalanceStrategy(context);

        ServiceInstance instance = balanceStrategy.doSelect(context, serviceInstances);

        context.setAttr(SERVICE_IN_USE, instance);

        super.fireNext(context);
    }
}