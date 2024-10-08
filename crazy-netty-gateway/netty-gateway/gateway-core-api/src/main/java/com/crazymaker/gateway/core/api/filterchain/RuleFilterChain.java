package com.crazymaker.gateway.core.api.filterchain;

import com.crazymaker.gateway.core.api.context.GatewayContext;

import javax.servlet.FilterChain;
import java.util.List;

public interface RuleFilterChain {
    FilterChain getFilterChain(GatewayContext gatewayContext) throws Exception;

    /**
     * @一句话介绍： 为rule 添加过滤器
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @date
     * @详细介绍： 一个rule 对应多个 过滤器链条， 为 每一个链条 添加过滤器， 并且做必要的初始化
     */
    void addFilter(ProcessorFilterType filterType, List<Filter<GatewayContext>> filters) throws Exception;


    void doPreFilterChain(GatewayContext ctx) throws Throwable;

    void doErrorFilterChain(GatewayContext ctx) throws Exception;


    void doPostFilterChain(GatewayContext ctx) throws Throwable;
}
