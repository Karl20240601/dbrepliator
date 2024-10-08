package com.crazymaker.gateway.core.api.balance;

import com.crazymaker.gateway.core.api.metadata.Rule;
import com.crazymaker.micro.core.servcie.MicroCore;

/**
 * @ClassName BalanceStrategyFactory
 * @Description 加载负载均衡策略
 * @Author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 */

public interface BalanceStrategyFactory extends MicroCore {
    BalanceStrategy buildStrategyFactory(Rule rule);
}
