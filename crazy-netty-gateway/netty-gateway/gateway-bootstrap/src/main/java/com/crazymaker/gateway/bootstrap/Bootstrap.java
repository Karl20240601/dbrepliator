package com.crazymaker.gateway.bootstrap;

import com.crazymaker.gateway.config.ConfigLoader;
import com.crazymaker.gateway.core.api.config.Config;
import com.crazymaker.gateway.core.api.config.GlobalSingletons;
import com.crazymaker.gateway.core.api.processer.NettyProcessor;
import com.crazymaker.gateway.server.NettyHttpServer;
import com.crazymaker.micro.core.servcie.MicroCoreManager;
import lombok.extern.slf4j.Slf4j;

/**
 * API网关启动类
 */
@Slf4j
public class Bootstrap {
    private static Config config;

    private static NettyHttpServer nettyHttpServer;

    public static void main(String[] args) {
        //加载网关静态配置
        Config config = ConfigLoader.getInstance().load(args);
        System.out.println(config.getPort());


        try {
            MicroCoreManager.INSTANCE.boot(config);
        } catch (Exception e) {
            log.error("gateway boot failure.", e);
        }


        nettyHttpServer = new NettyHttpServer(config);

        JwtServiceBootstrap.start(config);

        //   启动netty服务
        nettyHttpServer.init();

        NettyProcessor nettyProcessor = GlobalSingletons.getNettyProcessor();
        nettyHttpServer.start(nettyProcessor);


        // 调用 注册微内核 ，注册实例到 注册中心 如 nacos/Zookeeper
    }

}
