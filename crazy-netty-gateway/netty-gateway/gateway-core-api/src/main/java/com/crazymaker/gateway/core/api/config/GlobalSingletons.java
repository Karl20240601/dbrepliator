package com.crazymaker.gateway.core.api.config;

import com.crazymaker.gateway.core.api.balance.BalanceStrategyFactory;
import com.crazymaker.gateway.core.api.filterchain.FilterChainFactory;
import com.crazymaker.gateway.core.api.metadata.RuleLoader;
import com.crazymaker.gateway.core.api.processer.NettyProcessor;
import com.crazymaker.gateway.core.api.upstream.ServiceManager;
import com.crazymaker.micro.core.servcie.MicroCoreManager;
import com.crazymaker.springcloud.common.util.AntPathMatcher;

public class GlobalSingletons {

    public static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();


    private static RuleLoader ruleLoaderSingleton;

    public static RuleLoader getRuleLoaderSingleton() {

        if (null == ruleLoaderSingleton) {
            synchronized (RuleLoader.class) {
                if (null == ruleLoaderSingleton) {
                    ruleLoaderSingleton = MicroCoreManager.INSTANCE.findService(RuleLoader.class);
                }
            }
        }
        return ruleLoaderSingleton;


    }

    private static FilterChainFactory filterFactory;

    public static FilterChainFactory getFilterFactory() {
        if (null == filterFactory) {
            synchronized (FilterChainFactory.class) {
                if (null == filterFactory) {
                    filterFactory = MicroCoreManager.INSTANCE.findService(FilterChainFactory.class);
                }
            }
        }
        return filterFactory;
    }

    private static NettyProcessor nettyProcessor;

    public static NettyProcessor getNettyProcessor() {
        if (null == nettyProcessor) {
            synchronized (NettyProcessor.class) {
                if (null == nettyProcessor) {
                    nettyProcessor = MicroCoreManager.INSTANCE.findService(NettyProcessor.class);
                }
            }
        }
        return nettyProcessor;
    }


    private static ServiceManager serviceManager;

    public static ServiceManager getServiceManager() {
        if (null == serviceManager) {
            synchronized (ServiceManager.class) {
                if (null == serviceManager) {
                    serviceManager = MicroCoreManager.INSTANCE.findService(ServiceManager.class);
                }
            }
        }
        return serviceManager;
    }


    private static BalanceStrategyFactory balanceStrategyFactory;

    public static BalanceStrategyFactory getBalanceStrategyFactory() {
        if (null == balanceStrategyFactory) {
            synchronized (BalanceStrategyFactory.class) {
                if (null == balanceStrategyFactory) {
                    balanceStrategyFactory = MicroCoreManager.INSTANCE.findService(BalanceStrategyFactory.class);
                }
            }
        }
        return balanceStrategyFactory;
    }
}