package com.crazymaker.gateway.strategy;

import com.crazymaker.gateway.core.api.balance.BalanceStrategy;
import com.crazymaker.gateway.core.api.balance.BalanceStrategyAnnotation;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.crazymaker.gateway.core.api.constant.BasicConst.BALANCE_STRATEGY_ROUND_ROBIN;


/**
 * @DESCRIPTION: 负载均衡- 轮询
 */


@BalanceStrategyAnnotation(type = BALANCE_STRATEGY_ROUND_ROBIN)
@Slf4j
public class RoundRobinStrategy implements BalanceStrategy {
    private AtomicInteger position = new AtomicInteger(1);

    /**
     * @param context
     * @param instances
     * @一句话介绍： 子类实现指定的负载均衡策略选择一个服务
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1.子类实现
     * 2.  指定的负载均衡策略,  从 instances 选择一个服务
     */
    @Override
    public ServiceInstance doSelect(GatewayContext context, List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return null;
        } else {
            int pos = Math.abs(this.position.incrementAndGet());
            return instances.get(pos % instances.size());
        }
    }

    @Override
    public BalanceStrategy newInstance() {
        return new RoundRobinStrategy();
    }
}
