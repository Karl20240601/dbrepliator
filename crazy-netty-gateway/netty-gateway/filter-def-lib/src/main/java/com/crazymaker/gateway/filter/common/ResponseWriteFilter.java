package com.crazymaker.gateway.filter.common;

import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.filterchain.DefaultLinkedFilter;
import com.crazymaker.gateway.core.api.filterchain.FilterAnnotation;
import com.crazymaker.gateway.core.api.util.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static com.crazymaker.gateway.core.api.constant.BasicConst.ROUTER_FILTER_ORDER;
import static com.crazymaker.gateway.core.api.filterchain.ProcessorFilterType.POST;

/**
 * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 * @一句话介绍： http 异步路由过滤器
 * @date
 * @详细介绍： http 异步路由过滤器
 */
@Slf4j
@FilterAnnotation(id = "ResponseWriteFilter",
        name = "",
        type = POST,
        order = ROUTER_FILTER_ORDER)
public class ResponseWriteFilter extends DefaultLinkedFilter<GatewayContext> {
    private static Logger accessLog = LoggerFactory.getLogger("accessLog");

    public void doFilter(GatewayContext gatewayContext, Object... args) throws Exception {

        gatewayContext.releaseRequest();
        Throwable throwable = gatewayContext.getThrowable();
        if (Objects.nonNull(throwable)) {
            log.error("流程错误 error，发生异常，不应该走这个 责任链", throwable);
        }
        gatewayContext.written();
        ResponseHelper.writeResponse(gatewayContext);

        //记录访问日志

        ResponseHelper.logAccess(gatewayContext);

    }
}