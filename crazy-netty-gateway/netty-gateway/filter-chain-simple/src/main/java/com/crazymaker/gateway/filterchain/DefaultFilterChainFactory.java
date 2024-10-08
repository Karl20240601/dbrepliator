package com.crazymaker.gateway.filterchain;

import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.filterchain.*;
import com.crazymaker.gateway.rule.DefaultRuleFilterChain;
import com.crazymaker.micro.core.annotation.DefaultImplementor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认过滤器工厂实现类
 */
@Slf4j
@DefaultImplementor

public class DefaultFilterChainFactory implements FilterChainFactory {

    //缓存所有的  过滤器
    private Map<String /*filterId*/, Filter> processorFilterIdMap = new ConcurrentHashMap<>();


    // 按照类型，缓存所有的过滤器
    Map<String, List<Filter<GatewayContext>>> typeFilterListMap = new LinkedHashMap<String, List<Filter<GatewayContext>>>();

    private Map<String /*ruleId*/, RuleFilterChain> ruleFilterChainMap = new ConcurrentHashMap<>();


    public DefaultFilterChainFactory() {


    }

    /**
     * @一句话介绍： 加载所有的ProcessorFilter子类的实现
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1. 加载所有的ProcessorFilter子类的实现
     * 2. 按照 pre route  error post   四种类型进行处理
     */
    private void loadFilters() {
        //	java SPI方式加载filter的集合：
        //	通过ServiceLoader加载
        ServiceLoader<Filter> serviceLoader = ServiceLoader.load(Filter.class);

        for (Filter<GatewayContext> filter : serviceLoader) {
            // 通过反射 或得 过滤器元数据
            FilterAnnotation annotation = filter.getClass().getAnnotation(FilterAnnotation.class);
            if (annotation != null) {
                String filterType = annotation.type().getCode();

                // 从map 里边去list，没有就创建
                List<Filter<GatewayContext>> filterList = typeFilterListMap.computeIfAbsent(filterType, type -> {

                    return new ArrayList<Filter<GatewayContext>>();
                });
                filterList.add(filter);

                //添加到过滤集合
                String id = annotation.id();
                if (StringUtils.isEmpty(id)) {
                    id = filter.getClass().getName();
                }
                processorFilterIdMap.put(id, filter);

                try {
                    if (filter.isGlobal()) {
                        filter.init();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /**
         * @一句话介绍： 按照 pre route  error post   四种类型进行  排序  处理
         * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
         * @详细介绍： todo
        四种类型：
        PRE("PRE", "前置过滤器"),
        ROUTE("ROUTE", "中置过滤器"),
        ERROR("ERROR", "前置过滤器"),
        POST("POST", "前置过滤器");
         */
        for (ProcessorFilterType filterType : ProcessorFilterType.values()) {
            // 通过类型，取出 列表
            List<Filter<GatewayContext>> filterList = typeFilterListMap.get(filterType.getCode());
            if (filterList == null || filterList.isEmpty()) {
                continue;
            }

            Collections.sort(filterList, new Comparator<Filter<GatewayContext>>() {
                @Override
                public int compare(Filter<GatewayContext> o1, Filter<GatewayContext> o2) {
                    return o1.getClass().getAnnotation(FilterAnnotation.class).order() -
                            o2.getClass().getAnnotation(FilterAnnotation.class).order();
                }
            });
        }
    }

    @Override
    public RuleFilterChain findFilterChain(GatewayContext gatewayContext) throws Exception {

        String ruleId = gatewayContext.getRule().getId();

        return ruleFilterChainMap.computeIfAbsent(ruleId, rId -> {

            return newFilterChain(rId);
        });


    }

    /**
     * @一句话介绍： todo
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @date
     * @详细介绍： 四种类型：
     * PRE("PRE", "前置过滤器"),
     * ROUTE("ROUTE", "中置过滤器"),
     * ERROR("ERROR", "前置过滤器"),
     * POST("POST", "前置过滤器");
     */

    private RuleFilterChain newFilterChain(String rId) {

        RuleFilterChain chain = new DefaultRuleFilterChain();


        for (ProcessorFilterType filterType : ProcessorFilterType.values()) {
            List<Filter<GatewayContext>> filterList = typeFilterListMap.get(filterType.getCode());
            if (filterList == null || filterList.isEmpty()) {
                continue;
            }
            try {
                chain.addFilter(filterType, filterList);
            } catch (Exception e) {
                //	ignor
                log.error("网关过滤器加载异常, 异常信息为：{}!", e.getMessage(), e);
            }
        }

        return chain;
    }


    @SuppressWarnings("unchecked")
    public <T> T getFilter(String filterId) throws Exception {
        Filter<GatewayContext> filter = processorFilterIdMap.get(filterId);
        return (T) filter;
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

    }

    @Override
    public void boot() throws Throwable {
        log.info("micro core is booted:" + DefaultFilterChainFactory.class);
        loadFilters();
    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {

    }
}
