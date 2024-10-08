package com.crazymaker.gateway.core.api.constant;

import com.crazymaker.gateway.core.api.filterchain.RuleFilterChain;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;
import io.netty.util.AttributeKey;

import java.util.List;

public interface ContextConst {


    /**
     * @一句话介绍： 上下文的  过滤器责任链
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1.  CONTEXT  请求上下文
     * 2.  RULE_FILTER_CHAIN    过滤器责任链
     */
    AttributeKey<RuleFilterChain> CONTEXT_RULE_FILTER_CHAIN = AttributeKey.valueOf("CONTEXT_RULE_FILTER_CHAIN");
    /**
     * @一句话介绍： 上下文的  服务实例 列表
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1.  CONTEXT  请求上下文
     * 2.  SERVICE_INSTANCES    服务实例列表
     */
    AttributeKey<List<ServiceInstance>> SERVICE_INSTANCES = AttributeKey.valueOf("SERVICE_INSTANCES");

    /**
     * @一句话介绍： 上下文的  服务实例
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1.  CONTEXT  请求上下文
     * 2.  SERVICE_INSTANCES    服务实例
     */
    AttributeKey<ServiceInstance> SERVICE_IN_USE = AttributeKey.valueOf("SERVICE_IN_USE");


}