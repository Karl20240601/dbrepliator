package com.crazymaker.gateway.rule;

import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.filterchain.*;
import com.crazymaker.gateway.filterchain.DefaultFilterChain;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DefaultRuleFilterChain implements RuleFilterChain {

    /*
     *	pre + route + post
     */

    /** 前置责任链 ：  专门 转配 前置过滤器 **/
    public DefaultFilterChain preFilterSubChain = new DefaultFilterChain("defaultProcessorFilterChain");


    /*
     *	post
     */
    /** 后置责任链 ：  专门 转配 后置过滤器 **/

    public DefaultFilterChain postFilterSubChain = new DefaultFilterChain("postProcessorFilterChain");

    /*
     * 	error
     */
    public DefaultFilterChain errorFilterSubChain = new DefaultFilterChain("errorProcessorFilterChain");


    /*
     * 	根据过滤器id获取对应的Filter
     */
    public Map<String /* filterId */, Filter<GatewayContext>> processorFilterIdMap = new LinkedHashMap<>();


    public <T> T getFilter(Class<T> t) throws Exception {
        FilterAnnotation annotation = t.getAnnotation(FilterAnnotation.class);
        if (annotation != null) {
            String filterId = annotation.id();
            if (filterId == null || filterId.length() < 1) {
                filterId = t.getName();
            }
            return this.getFilter(filterId);
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    public <T> T getFilter(String filterId) throws Exception {
        Filter<GatewayContext> filter = null;
        if (!processorFilterIdMap.isEmpty()) {
            filter = processorFilterIdMap.get(filterId);
        }
        return (T) filter;
    }


    @Override
    public FilterChain getFilterChain(GatewayContext gatewayContext) throws Exception {
        return null;
    }


    /**
     * @param filterType
     * @param filters
     * @一句话介绍： 为rule 添加过滤器
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @date
     * @详细介绍： 一个rule 对应多个 过滤器链条， 为 每一个链条 添加过滤器， 并且做必要的初始化
     */
    @Override
    public void addFilter(ProcessorFilterType filterType, List<Filter<GatewayContext>> filters) throws Exception {

        switch (filterType) {
            case PRE:
            case ROUTE:
                // 添加在 前置 过滤器 子链， 处理下游请求的报文， 然后进行路由转发，异步转发到上游
                appendFilter2List(preFilterSubChain, filters);
                break;
            case ERROR:
                // 添加在 错误处理 过滤器 子链 ， 处理 异步错误
                appendFilter2List(errorFilterSubChain, filters);
                break;
            case POST:

                // 添加在 后置处理 过滤器 子链 ， 处理来自上游的 响应报文
                appendFilter2List(postFilterSubChain, filters);
                break;
            default:
                throw new RuntimeException("ProcessorFilterType is not supported !");
        }

    }

    /**
     * @一句话介绍： 添加 到 过滤器链
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @date
     * @详细介绍： 1 添加 到 过滤器链
     * 2 并且初始化 过滤器
     */
    private void appendFilter2List(DefaultFilterChain processorFilterChain,
                                   List<Filter<GatewayContext>> filters) throws Exception {
        for (Filter<GatewayContext> filter : filters) {
            Filter<GatewayContext> target = filter;

            //局部过滤器，创建一个实例，然后初始化
            if (!filter.isGlobal()) {
                target = filter.newInstance();
                target.init();
            }

            FilterAnnotation annotation = target.getClass().getAnnotation(FilterAnnotation.class);

            if (annotation != null) {
                //	构建过滤器链条，添加filter
                processorFilterChain.addLast((DefaultLinkedFilter<GatewayContext>) target);

                //	映射到过滤器集合
                String filterId = annotation.id();
                if (filterId == null || filterId.length() < 1) {
                    filterId = target.getClass().getName();
                }

                //	id
                processorFilterIdMap.put(filterId, target);
            }

        }
    }


    /**
     * <B>方法名称：</B>doFilterChain<BR>
     * <B>概要说明：</B>正常过滤器链条执行：pre + route + post<BR>
     */
    @Override
    public void doPreFilterChain(GatewayContext ctx) throws Throwable {

        preFilterSubChain.doFilter(ctx);

    }


    @Override
    public void doErrorFilterChain(GatewayContext ctx) throws Exception {
        try {
            errorFilterSubChain.doFilter(ctx);
        } catch (Throwable e) {
            log.error("#DefaultProcessorFilterFactory.doErrorFilterChain# ERROR MESSAGE: {}", e.getMessage(), e);
        }
    }

    /**
     * @一句话介绍： 执行后置过滤器  链条
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @date 2023-09-19
     * @详细介绍： 1.  执行后置过滤器链条
     * 2.   后置过滤器的最后一个环节，是写入netty channel
     */
    @Override
    public void doPostFilterChain(GatewayContext ctx) throws Throwable {

        postFilterSubChain.doFilter(ctx);

    }


}
