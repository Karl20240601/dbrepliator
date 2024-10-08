package com.crazymaker.gateway.process;

import com.crazymaker.gateway.core.api.constant.ResponseCode;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.context.GatewayResponse;
import com.crazymaker.gateway.core.api.context.HttpRequestWrapper;
import com.crazymaker.gateway.core.api.error.BizException;
import com.crazymaker.gateway.core.api.error.ResponseException;
import com.crazymaker.gateway.core.api.filterchain.FilterChainFactory;
import com.crazymaker.gateway.core.api.filterchain.RuleFilterChain;
import com.crazymaker.gateway.core.api.processer.NettyProcessor;
import com.crazymaker.gateway.core.api.util.RequestHelper;
import com.crazymaker.gateway.core.api.util.ResponseHelper;
import com.crazymaker.micro.core.annotation.DefaultImplementor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.extern.slf4j.Slf4j;

import static com.crazymaker.gateway.core.api.config.GlobalSingletons.getFilterFactory;
import static com.crazymaker.gateway.core.api.constant.ContextConst.CONTEXT_RULE_FILTER_CHAIN;
import static com.crazymaker.gateway.core.api.util.ResponseHelper.directWriteAndRelease;

// 对应到netty 应用开发当中的 业务环节
// 理论上不能在IO线程上执行，需要独立的线程池异步执行

@DefaultImplementor
@Slf4j
public class NettyCoreProcessor implements NettyProcessor {

    private FilterChainFactory chainFactory;

    @Override
    public void process(HttpRequestWrapper wrapper) {
        FullHttpRequest request = wrapper.getRequest();
        ChannelHandlerContext ctx = wrapper.getCtx();

        try {
            GatewayContext gatewayContext = RequestHelper.buildContext(request, ctx);
            //根据上下文去找 过滤器责任链
            RuleFilterChain filterChain = chainFactory.findFilterChain(gatewayContext);

            // 把这个过滤器责任链 放在上下文 缓存起来
            gatewayContext.setAttr(CONTEXT_RULE_FILTER_CHAIN, filterChain);

            // 进行 请求的 责任链 处理
            filterChain.doPreFilterChain(gatewayContext);

        } catch (ResponseException t) {
            log.error(" error", t);
            FullHttpResponse httpResponse = ResponseHelper.buildHttpResponse(t.getCode());
            directWriteAndRelease(ctx, request, httpResponse);
            log.debug("complete error", t);

        } catch (BizException e) {
            e.printStackTrace();
            log.error("path {} ,process error {} {}", request.getUri(), e.getCode().getCode(), e.getCode().getMessage());
            FullHttpResponse httpResponse = ResponseHelper.buildHttpResponse(e.getCode());
            directWriteAndRelease(ctx, request, httpResponse);
        } catch (Throwable t) {
            log.error(" error", t);
            FullHttpResponse httpResponse = ResponseHelper.buildHttpResponse(ResponseCode.INTERNAL_ERROR);
            directWriteAndRelease(ctx, request, httpResponse);
        }


    }


    @Override
    public void start() {

    }

    @Override
    public void shutDown() {

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
        if (null == chainFactory) {
            chainFactory = getFilterFactory();
        }
        if (null == chainFactory) {
            log.error("micro core is  error : 没有加载到过滤器工厂内核");
        }

    }

    @Override
    public void boot() throws Throwable {
        log.info("micro core is booted:" + NettyCoreProcessor.class);

    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {

    }
}
