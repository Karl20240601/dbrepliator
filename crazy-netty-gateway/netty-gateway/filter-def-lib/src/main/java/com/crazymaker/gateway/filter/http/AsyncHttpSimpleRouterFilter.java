package com.crazymaker.gateway.filter.http;

import com.crazymaker.gateway.core.api.constant.ResponseCode;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.context.GatewayResponse;
import com.crazymaker.gateway.core.api.error.ResponseException;
import com.crazymaker.gateway.core.api.filterchain.DefaultLinkedFilter;
import com.crazymaker.gateway.core.api.filterchain.FilterAnnotation;
import com.crazymaker.gateway.core.api.filterchain.RuleFilterChain;
import com.crazymaker.gateway.core.api.util.AsyncHttpHelper;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static com.crazymaker.gateway.core.api.constant.BasicConst.ROUTER_FILTER_ORDER;
import static com.crazymaker.gateway.core.api.constant.ContextConst.CONTEXT_RULE_FILTER_CHAIN;
import static com.crazymaker.gateway.core.api.filterchain.ProcessorFilterType.ROUTE;

/**
 * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 * @一句话介绍： http 异步路由过滤器
 * @date
 * @详细介绍： http 异步路由过滤器
 */
@Slf4j
@FilterAnnotation(id = "AsyncHttpSimpleRouterFilter",
        name = "",
        type = ROUTE,
        order = ROUTER_FILTER_ORDER)
public class AsyncHttpSimpleRouterFilter extends DefaultLinkedFilter<GatewayContext> {
    private static Logger accessLog = LoggerFactory.getLogger("accessLog");

    public void doFilter(GatewayContext gatewayContext, Object... args) throws Exception {
        RequestBuilder requestBuilder = AsyncHttpHelper.getInstance().getRequestBuilder(gatewayContext);
        // 构造一个异步http请求对象，去请求上游的服务
        //  asynchttpclient 组件，是异步，是用专用线程池去执行  http请求
        Request request = requestBuilder.build();
        // 返回 一个future 异步任务
        CompletableFuture<Response> future = AsyncHttpHelper.getInstance().executeRequest(request);

        //设置异步任务的回调
        future.whenCompleteAsync((response, throwable) -> {
            complete(request, response, throwable, gatewayContext);
        });

    }

    // 这个回调是 asynchttpclient 所绑定的线程来执行

    private void complete(Request request,
                          Response response,
                          Throwable throwable,
                          GatewayContext gatewayContext) {

        //从上文拿到 过滤器责任链
        RuleFilterChain filterChain = gatewayContext.attr(CONTEXT_RULE_FILTER_CHAIN);
        boolean hasError = false;
        try {
            if (Objects.nonNull(throwable)) {
                String url = request.getUrl();
                if (throwable instanceof TimeoutException) {
                    log.warn("complete time out {}", url);
                    gatewayContext.setThrowable(new ResponseException(ResponseCode.REQUEST_TIMEOUT));
                    gatewayContext.setResponse(GatewayResponse.buildGatewayResponse(ResponseCode.REQUEST_TIMEOUT));
                } else {
                    gatewayContext.setThrowable(new ResponseException(ResponseCode.HTTP_RESPONSE_ERROR));
                    gatewayContext.setResponse(GatewayResponse.buildGatewayResponse(ResponseCode.HTTP_RESPONSE_ERROR));
                }
            } else {

                GatewayResponse gatewayResponse = GatewayResponse.buildGatewayResponse(response);
                gatewayContext.setResponse(gatewayResponse);
            }
        } catch (ResponseException t) {

            hasError = true;
            gatewayContext.setThrowable(t);
            gatewayContext.setResponse(GatewayResponse.buildGatewayResponse(ResponseCode.INTERNAL_ERROR));
            log.debug("complete error", t);

        }catch (Throwable t) {

            hasError = true;
            gatewayContext.setThrowable(new ResponseException(ResponseCode.INTERNAL_ERROR));
            gatewayContext.setResponse(GatewayResponse.buildGatewayResponse(ResponseCode.INTERNAL_ERROR));
            log.error("complete error", t);
        }

        /**
         * @一句话介绍： 开始走后置过滤器链条
         * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
         * 1. 已经走完了前置过滤器
         * 2.  没有发生异常 ， 开始走后置过滤器链条
         */


        try {

            if (hasError) {
                // 执行 错误处理责任链
                filterChain.doErrorFilterChain(gatewayContext);
            } else {

                // 执行 后置责任链
                filterChain.doPostFilterChain(gatewayContext);

            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
