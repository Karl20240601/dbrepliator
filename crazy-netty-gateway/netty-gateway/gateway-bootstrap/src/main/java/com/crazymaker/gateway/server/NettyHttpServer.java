package com.crazymaker.gateway.server;

import com.crazymaker.gateway.core.api.config.Config;
import com.crazymaker.gateway.core.api.processer.NettyProcessor;
import com.crazymaker.gateway.handler.NettyHttpServerHandler;
import com.crazymaker.springcloud.common.util.RemotingUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 * @详细介绍：
 * 1.
 * 2. 测试地址1： http://localhost:8888/ping
 */
@Slf4j
public class NettyHttpServer {
    private final Config config;
    private ServerBootstrap serverBootstrap;
    private EventLoopGroup eventLoopGroupBoss;

    @Getter
    private EventLoopGroup eventLoopGroupWoker;


    public NettyHttpServer(Config config) {
        this.config = config;

    }


    public void init() {
        this.serverBootstrap = new ServerBootstrap();
        if (RemotingUtil.canUseEpoll()) {
            this.eventLoopGroupBoss = new EpollEventLoopGroup(config.getEventLoopGroupBossNum(),
                    new DefaultThreadFactory("netty-boss-nio"));
            this.eventLoopGroupWoker = new EpollEventLoopGroup(config.getEventLoopGroupWokerNum(),
                    new DefaultThreadFactory("netty-worker-nio"));
        } else {
            this.eventLoopGroupBoss = new NioEventLoopGroup(config.getEventLoopGroupBossNum(),
                    new DefaultThreadFactory("netty-boss-nio"));
            this.eventLoopGroupWoker = new NioEventLoopGroup(config.getEventLoopGroupWokerNum(),
                    new DefaultThreadFactory("netty-worker-nio"));
        }
    }

    public void start(NettyProcessor nettyProcessor) {
        this.serverBootstrap
                .group(eventLoopGroupBoss, eventLoopGroupWoker)
                .channel(RemotingUtil.canUseEpoll() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)            //	sync + accept = backlog
                .option(ChannelOption.SO_REUSEADDR, true)    //	tcp端口重绑定
                .option(ChannelOption.SO_KEEPALIVE, false)    //  如果在两小时内没有数据通信的时候，TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.TCP_NODELAY, true)   //	该参数的左右就是禁用Nagle算法，使用小数据传输时合并
                .childOption(ChannelOption.SO_SNDBUF, 65535)    //	设置发送数据缓冲区大小
                .childOption(ChannelOption.SO_RCVBUF, 65535)    //	设置接收数据缓冲区大小
                .localAddress(new InetSocketAddress(config.getPort()))
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(
                                new HttpServerCodec(), //http编解码
                                new HttpObjectAggregator(config.getMaxContentLength()), //请求报文聚合成FullHttpRequest
                                new HttpServerExpectContinueHandler(),
                                new NettyHttpServerHandler(nettyProcessor)
                        );
                    }
                });

        try {
            this.serverBootstrap.bind().sync();
            log.info("server startup on port {}", this.config.getPort());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void shutdown() {
        if (eventLoopGroupBoss != null) {
            eventLoopGroupBoss.shutdownGracefully();
        }

        if (eventLoopGroupWoker != null) {
            eventLoopGroupWoker.shutdownGracefully();
        }
    }
}
