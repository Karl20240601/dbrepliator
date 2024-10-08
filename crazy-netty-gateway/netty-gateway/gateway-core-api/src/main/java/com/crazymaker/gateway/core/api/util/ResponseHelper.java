package com.crazymaker.gateway.core.api.util;


import com.crazymaker.gateway.core.api.constant.BasicConst;
import com.crazymaker.gateway.core.api.constant.ResponseCode;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.context.GatewayResponse;
import com.crazymaker.springcloud.common.util.TimeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 响应的辅助类
 */
public class ResponseHelper {
    private static Logger accessLog = LoggerFactory.getLogger("accessLog");

    /**
     * 获取响应对象
     **/

    public static FullHttpResponse buildHttpResponse(ResponseCode responseCode) {
        GatewayResponse resRapidResponse = GatewayResponse.buildGatewayResponse(responseCode);
        DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                Unpooled.wrappedBuffer(resRapidResponse.getContent().getBytes()));

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
        return httpResponse;
    }

    /**
     * @一句话介绍： 通过上下文对象和RapidResponse对象 构建FullHttpResponse
     */
    public static FullHttpResponse buildHttpResponse(GatewayContext ctx, GatewayResponse GatewayResponse) {
        ByteBuf content;
        if (Objects.nonNull(GatewayResponse.getFutureResponse())) {
            content = Unpooled.wrappedBuffer(GatewayResponse.getFutureResponse()
                    .getResponseBodyAsByteBuffer());
        } else if (GatewayResponse.getContent() != null) {
            content = Unpooled.wrappedBuffer(GatewayResponse.getContent().getBytes());
        } else {
            content = Unpooled.wrappedBuffer(BasicConst.BLANK_SEPARATOR_1.getBytes());
        }

        if (Objects.isNull(GatewayResponse.getFutureResponse())) {
            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    GatewayResponse.getHttpResponseStatus(),
                    content);
            httpResponse.headers().add(GatewayResponse.getResponseHeaders());
            httpResponse.headers().add(GatewayResponse.getExtraResponseHeaders());
            httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
            return httpResponse;
        } else {
            GatewayResponse.getFutureResponse().getHeaders().add(GatewayResponse.getExtraResponseHeaders());

            DefaultFullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.valueOf(GatewayResponse.getFutureResponse().getStatusCode()),
                    content);
            httpResponse.headers().add(GatewayResponse.getFutureResponse().getHeaders());
            return httpResponse;
        }
    }


    public static void writeResponse(GatewayContext context) {

        //	设置SS:
        context.setSSTime(TimeUtil.currentTimeMillis());

        //	释放资源
        context.releaseRequest();

        if (context.isWritten()) {
            //	1：第一步构建响应对象，并写回数据
            FullHttpResponse httpResponse = ResponseHelper.buildHttpResponse(context, context.getResponse());
            writeHttpResponse(context, httpResponse);
        }


    }

    /**
     * @一句话介绍： 写结果
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍：
     * 1.  短连接 写完后，关闭连接
     * 2.   长连接  写完后，不关闭
     */
    public static void writeHttpResponse(GatewayContext context, FullHttpResponse httpResponse) {
        if (!context.isKeepAlive()) {
            context.getNettyCtx()
                    .writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
        }
        //	长连接：
        else {
            httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            context.getNettyCtx().writeAndFlush(httpResponse);
        }
        //	2:	设置写回结束状态为： COMPLETED
        context.completed();
    }

    /**
     * @一句话介绍： 写后，关闭channel
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍：
     * 1.
     * 2.
     */

    public static void directWriteAndRelease(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse httpResponse) {

        ctx.writeAndFlush(httpResponse)
                .addListener(ChannelFutureListener.CLOSE);
        ReferenceCountUtil.release(request);
    }

    public static void logAccess(GatewayContext gatewayContext) {
        accessLog.info("{} {} {} {} {} {} {}",
                TimeUtil.currentTimeMillis() - gatewayContext.getRequest().getBeginTime(),
                gatewayContext.getRequest().getClientIp(),
                gatewayContext.getRule().getId(),
                gatewayContext.getRequest().getMethod(),
                gatewayContext.getRequest().getPath(),
                gatewayContext.getResponse().getHttpResponseStatus().code(),
                gatewayContext.getResponse().getFutureResponse().getResponseBodyAsBytes().length);

    }
}
