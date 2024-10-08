package com.crazymaker.gateway.core.api.util;

import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.context.GatewayRequest;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;
import io.netty.buffer.ByteBuf;
import org.asynchttpclient.*;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.crazymaker.gateway.core.api.constant.ContextConst.SERVICE_IN_USE;

/**
 * 异步的http辅助类
 */
public class AsyncHttpHelper {

    private static final class SingletonHolder {
        private static final AsyncHttpHelper INSTANCE = new AsyncHttpHelper();
    }

    private AsyncHttpHelper() {

    }

    public static AsyncHttpHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private AsyncHttpClient asyncHttpClient;

    public void initialized(AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }

    public CompletableFuture<Response> executeRequest(Request request) {
        ListenableFuture<Response> future = asyncHttpClient.executeRequest(request);
        return future.toCompletableFuture();
    }

    public <T> CompletableFuture<T> executeRequest(Request request, AsyncHandler<T> handler) {
        ListenableFuture<T> future = asyncHttpClient.executeRequest(request, handler);
        return future.toCompletableFuture();
    }


    /**
     * @一句话介绍： 构建 upstream 请求,
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @date
     * @详细介绍： 构建 upstream 请求,  http请求构建器
     */
    public RequestBuilder getRequestBuilder(GatewayContext gatewayContext) {
        GatewayRequest gatewayRequest = gatewayContext.getRequest();
        RequestBuilder requestBuilder = new RequestBuilder();
        requestBuilder.setMethod(gatewayRequest.getMethod().name());
        requestBuilder.setHeaders(gatewayRequest.getHeaders());
        requestBuilder.setQueryParams(gatewayRequest.getQueryDecoder().parameters());
        ByteBuf contentBuffer = gatewayRequest.getFullHttpRequest().content();
        if (Objects.nonNull(contentBuffer)) {
            requestBuilder.setBody(contentBuffer.nioBuffer());
        }

        ServiceInstance inUse = gatewayContext.attr(SERVICE_IN_USE);
        String finalUrl = inUse.getFinalUrl();
        requestBuilder.setUrl(finalUrl);

        return requestBuilder;
    }

}
