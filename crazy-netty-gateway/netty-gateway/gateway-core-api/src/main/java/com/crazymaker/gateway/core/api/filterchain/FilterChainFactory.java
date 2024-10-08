package com.crazymaker.gateway.core.api.filterchain;

import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.micro.core.servcie.MicroCore;

/**
 * 过滤器工厂接口
 */
public interface FilterChainFactory extends MicroCore {

    /**
     * 根据过滤器类型，添加一组过滤器，用于构建过滤器链
     */
    RuleFilterChain findFilterChain(GatewayContext gatewayContext) throws Exception;


    /**
     * 获取指定ID的过滤器
     */
    <T> T getFilter(String filterId) throws Exception;


}
