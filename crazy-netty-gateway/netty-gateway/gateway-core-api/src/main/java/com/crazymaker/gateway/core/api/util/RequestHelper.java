package com.crazymaker.gateway.core.api.util;

import com.crazymaker.gateway.core.api.config.GlobalSingletons;
import com.crazymaker.gateway.core.api.constant.BasicConst;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.context.GatewayRequest;
import com.crazymaker.gateway.core.api.metadata.Rule;
import com.crazymaker.gateway.core.api.metadata.RuleLoader;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


public class RequestHelper {


    public static GatewayContext buildContext(FullHttpRequest request, ChannelHandlerContext ctx) {


        //	1. 	构建请求对象 GatewayRequest
        GatewayRequest gatewayRequest = buildRequest(request, ctx);

        //	2.	根据请求对象里的 uri，获取 route rule 定义
        Rule rule = getRouteRule(gatewayRequest);


        //	3. 	构建我们而定RapidContext对象
        GatewayContext rapidContext = GatewayContext.builder()
                .protocol(rule.getProtocol())
                .request(gatewayRequest)
                .nettyCtx(ctx)
                .keepAlive(HttpUtil.isKeepAlive(request))
                .rule(rule)
                .SRTime(gatewayRequest.getBeginTime())
                .build();


        return rapidContext;
    }


    private static GatewayRequest buildRequest(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {

        HttpHeaders headers = fullHttpRequest.headers();

        String host = headers.get(HttpHeaderNames.HOST);
        HttpMethod method = fullHttpRequest.method();
        String uri = fullHttpRequest.uri();
        String clientIp = getClientIp(ctx, fullHttpRequest);
        String contentType = HttpUtil.getMimeType(fullHttpRequest) == null ? null : HttpUtil.getMimeType(fullHttpRequest).toString();
        Charset charset = HttpUtil.getCharset(fullHttpRequest, StandardCharsets.UTF_8);

        GatewayRequest rapidRequest = new GatewayRequest(
                charset,
                clientIp,
                host,
                uri,
                method,
                contentType,
                headers,
                fullHttpRequest);

        return rapidRequest;
    }

    private static String getClientIp(ChannelHandlerContext ctx, FullHttpRequest request) {
        String xForwardedValue = request.headers().get(BasicConst.HTTP_FORWARD_SEPARATOR);

        String clientIp = null;
        if (StringUtils.isNotEmpty(xForwardedValue)) {
            List<String> values = Arrays.asList(xForwardedValue.split(", "));
            if (values.size() >= 1 && StringUtils.isNotBlank(values.get(0))) {
                clientIp = values.get(0);
            }
        }
        if (clientIp == null) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
            clientIp = inetSocketAddress.getAddress().getHostAddress();
        }
        return clientIp;
    }

    /**
     * 根据请求对象里的 uri，获取 route rule 定义
     *
     * @param rapidRequest 请求
     * @return
     */
    private static Rule getRouteRule(GatewayRequest rapidRequest) {
        // 获取	Rule
        // 从哪里获取？
        // 从 全局的 Rule loader 微内核获取
        // Rule loader  是可以拔插的 ，可以有很多版本， 比如通过db管理配置， 比如通过配置文件管理配置
        RuleLoader ruleLoader = GlobalSingletons.getRuleLoaderSingleton();
        return ruleLoader.findRule(rapidRequest);

    }


}
