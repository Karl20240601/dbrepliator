package com.crazymaker.gateway.service.manager;

import com.crazymaker.gateway.core.api.balance.BalanceStrategy;
import com.crazymaker.gateway.core.api.balance.BalanceStrategyFactory;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.metadata.Rule;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;
import com.crazymaker.gateway.core.api.upstream.ServiceManager;
import com.crazymaker.micro.core.annotation.DefaultImplementor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.crazymaker.gateway.core.api.config.GlobalSingletons.getBalanceStrategyFactory;
import static com.crazymaker.springcloud.common.util.BeanUtil.toList;

/**
 * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 * @一句话介绍： 动态 服务  管理类
 * @详细介绍： 1.  动态 服务  管理类
 * 2.  动态 服务  缓存类
 */
@Slf4j
@DefaultImplementor

public class DefaultServiceManager implements ServiceManager {


    //	服务的实例集合：serviceId 与 服务实例列表 对应
    private ConcurrentHashMap<String /* serviceId */ , List<ServiceInstance>> serviceInstanceMap = new ConcurrentHashMap<>();


    //	服务的实例集合：serviceId 与 负载均衡 对应
    private ConcurrentHashMap<String /* serviceId */ , BalanceStrategy> balanceStrategyMap = new ConcurrentHashMap<>();


    public List<ServiceInstance> getServiceInstances(GatewayContext context) {
        String serviceId = context.getRule().getServiceId();

        boolean gray = context.isGray();

        List<ServiceInstance> serviceInstances = serviceInstanceMap.computeIfAbsent(serviceId, k -> {
            return buildServiceList(context);
        });

        if (gray) {
            return serviceInstances.stream()
                    .filter(ServiceInstance::isGray)
                    .collect(Collectors.toList());
        }

        return serviceInstances;
    }

    private List<ServiceInstance> buildServiceList(GatewayContext context) {
        Rule rule = context.getRule();
        if (rule.isHttpService()) {
            return toList(rule.simpleServiceInstance());
        }

        return new ArrayList<>();
    }

    @Override
    public BalanceStrategy getBalanceStrategy(GatewayContext context) {

        String serviceId = context.getRule().getServiceId();

        BalanceStrategy balanceStrategy = balanceStrategyMap.computeIfAbsent(serviceId, k -> {
            return buildBalanceStrategy(context);
        });
        return balanceStrategy;
    }

    private BalanceStrategy buildBalanceStrategy(GatewayContext context) {

        Rule rule = context.getRule();
        BalanceStrategyFactory balanceStrategyFactory = getBalanceStrategyFactory();

        return balanceStrategyFactory.buildStrategyFactory(rule);

    }


    // 来自于微内核
    @Override
    public String getType() {
        return null;
    }

    @Override
    public void prepare(Object params) throws Throwable {

    }

    @Override
    public void boot() throws Throwable {
        log.info("micro core is booted:" + DefaultServiceManager.class);


    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {

    }

}
