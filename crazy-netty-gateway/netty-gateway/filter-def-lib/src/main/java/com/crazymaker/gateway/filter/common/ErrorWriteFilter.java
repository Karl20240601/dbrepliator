package com.crazymaker.gateway.filter.common;

import com.crazymaker.gateway.core.api.constant.ResponseCode;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.filterchain.DefaultLinkedFilter;
import com.crazymaker.gateway.core.api.filterchain.FilterAnnotation;
import com.crazymaker.gateway.core.api.util.ResponseHelper;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.crazymaker.gateway.core.api.constant.BasicConst.ROUTER_FILTER_ORDER;
import static com.crazymaker.gateway.core.api.filterchain.ProcessorFilterType.ERROR;

/**
 * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 * @一句话介绍：  错误处理 过滤器
 * @date
 * @详细介绍：  错误处理 过滤器
 */
@Slf4j
@FilterAnnotation(id = "ErrorWriteFilter",
        name = "",
        type = ERROR,
        order = ROUTER_FILTER_ORDER)
public class ErrorWriteFilter extends DefaultLinkedFilter<GatewayContext> {

    public void doFilter(GatewayContext gatewayContext, Object... args) throws Exception {

        gatewayContext.releaseRequest();
        Throwable throwable = gatewayContext.getThrowable();
        if (Objects.isNull(throwable)) {
            log.error("流程错误 error，发生异常，不应该走这个 责任链 处理器");
        }
        gatewayContext.written();
        FullHttpResponse httpResponse = ResponseHelper.buildHttpResponse(ResponseCode.INTERNAL_ERROR);
        ResponseHelper.writeHttpResponse(gatewayContext,httpResponse);
        //记录访问日志
        ResponseHelper.logAccess(gatewayContext);

    }
}