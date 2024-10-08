package com.crazymaker.gateway.core.api.config;

import lombok.Data;

@Data
public class Config {
    private int port = 8888;

    private int prometheusPort = 18000;

    private String applicationName = "api-gateway";
    private String namespace = "gateway";

    private String registryAddress = "cdh1:8848";

//    private String groupName = "dev";
    private String groupName = "public";


    private String publicKeyFile="D:/dev/proxy/crazy-netty-gateway/rsa-key/id_key_rsa.pub";
    private String privateKeyFile="D:/dev/proxy/crazy-netty-gateway/rsa-key/id_key_rsa";
    //netty

    private int eventLoopGroupBossNum = 1;

    private int eventLoopGroupWokerNum = Runtime.getRuntime().availableProcessors() *2;

    private int maxContentLength = 64 * 1024 * 1024;

    //默认单异步模式
    private boolean whenComplete = true;

    //	Http Async 参数选项：

    //	连接超时时间
    private int httpConnectTimeout = 30 * 1000;

    //	请求超时时间
    private int httpRequestTimeout = 30 * 1000;

    //	客户端请求重试次数
    private int httpMaxRequestRetry = 2;

    //	客户端请求最大连接数
    private int httpMaxConnections = 10000;

    //	客户端每个地址支持的最大连接数
    private int httpConnectionsPerHost = 8000;

    //	客户端空闲连接超时时间, 默认60秒
    private int httpPooledConnectionIdleTimeout = 60 * 1000;

    private String bufferType = "parallel";

    private int bufferSize = 1024 * 16;

    private int processThread = Runtime.getRuntime().availableProcessors();

    private String waitStrategy = "blocking";

    public String getWaitStrategy() {
        return waitStrategy;
    }
    //扩展.......
}
