package com.crazymaker.gateway.client;

import com.crazymaker.gateway.core.api.client.HttpPClient;
import com.crazymaker.gateway.core.api.config.Config;
import com.crazymaker.gateway.core.api.util.AsyncHttpHelper;
import com.crazymaker.micro.core.annotation.DefaultImplementor;
import com.crazymaker.springcloud.common.util.RemotingUtil;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import java.io.IOException;

@DefaultImplementor
@Slf4j
public class AsyncHttpPClient implements HttpPClient {
    private Config config;

    private EventLoopGroup eventLoopGroupWoker;

    private AsyncHttpClient asyncHttpClient;


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
        this.config = (Config) params;

    }

    @Override
    public void boot() throws Throwable {


        if (RemotingUtil.canUseEpoll()) {
            this.eventLoopGroupWoker = new EpollEventLoopGroup(config.getEventLoopGroupWokerNum(),
                    new DefaultThreadFactory("netty-httpclient-nio"));
        } else {
            this.eventLoopGroupWoker = new NioEventLoopGroup(config.getEventLoopGroupWokerNum(),
                    new DefaultThreadFactory("netty-httpclient-nio"));
        }

        DefaultAsyncHttpClientConfig.Builder builder = new DefaultAsyncHttpClientConfig.Builder()
                .setEventLoopGroup(eventLoopGroupWoker)
                .setConnectTimeout(config.getHttpConnectTimeout())
                .setRequestTimeout(config.getHttpRequestTimeout())
                .setMaxRedirects(config.getHttpMaxRequestRetry())
                .setAllocator(PooledByteBufAllocator.DEFAULT) //池化的byteBuf分配器，提升性能
                .setCompressionEnforced(true)
                .setMaxConnections(config.getHttpMaxConnections())
                .setMaxConnectionsPerHost(config.getHttpConnectionsPerHost())
                .setPooledConnectionIdleTimeout(config.getHttpPooledConnectionIdleTimeout());
        this.asyncHttpClient = new DefaultAsyncHttpClient(builder.build());


        AsyncHttpHelper.getInstance().initialized(asyncHttpClient);
    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() {
        if (asyncHttpClient != null) {
            try {
                this.asyncHttpClient.close();
            } catch (IOException e) {
                log.error("NettyHttpClient shutdown error", e);
            }
        }
    }

}
