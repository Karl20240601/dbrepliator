package com.crazymaker.gateway.balance;

import com.crazymaker.gateway.core.api.balance.BalanceStrategy;
import com.crazymaker.gateway.core.api.balance.BalanceStrategyAnnotation;
import com.crazymaker.gateway.core.api.balance.BalanceStrategyFactory;
import com.crazymaker.gateway.core.api.metadata.Rule;
import com.crazymaker.micro.core.annotation.DefaultImplementor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ServiceLoader;

import static com.crazymaker.gateway.core.api.constant.BasicConst.BALANCE_STRATEGY_ROUND_ROBIN;

/**
 * @ClassName BalanceStrategyFactory
 * @Description TODO
 * @Author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 */
@DefaultImplementor

public class DefaultBalanceStrategyFactory implements BalanceStrategyFactory {
    private Map<String /* type */, BalanceStrategy /* prototype*/> prototypeMap = new LinkedHashMap<>();

    @Override
    public BalanceStrategy buildStrategyFactory(Rule rule) {

        if (rule.getBalanceStrategy() != null) {
            BalanceStrategy proto = prototypeMap.get(rule.getBalanceStrategy());
            return proto.newInstance();
        }

        return prototypeMap.get(BALANCE_STRATEGY_ROUND_ROBIN).newInstance();
    }


    /**
     * 获取 微内核的 类型名称，
     * 和配置的名称相对应
     *
     * @return
     */
    @Override
    public String getType() {
        return null;
    }

    @Override
    public void prepare(Object params) throws Throwable {
        //	SPI方式加载filter的集合：
        //	通过ServiceLoader加载
        ServiceLoader<BalanceStrategy> serviceLoader = ServiceLoader.load(BalanceStrategy.class);

        for (BalanceStrategy strategy : serviceLoader) {
            BalanceStrategyAnnotation annotation = strategy.getClass().getAnnotation(BalanceStrategyAnnotation.class);
            if (annotation != null) {
                String strategyType = annotation.type();
                prototypeMap.put(strategyType, strategy);
            }
        }
    }

    @Override
    public void boot() throws Throwable {

    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {

    }


}
