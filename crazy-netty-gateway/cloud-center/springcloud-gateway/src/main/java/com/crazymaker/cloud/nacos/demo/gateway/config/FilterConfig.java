package com.crazymaker.cloud.nacos.demo.gateway.config;

import com.crazymaker.cloud.nacos.demo.gateway.filter.AuthGlobalFilter;
import com.crazymaker.cloud.nacos.demo.gateway.filter.HttpBodyGlobalFilter;
import com.crazymaker.cloud.nacos.demo.gateway.filter.PushMessageGlobalFilter;
import com.crazymaker.cloud.nacos.demo.gateway.filter.UserIdCheckGateWayFilter;
import com.crazymaker.springcloud.base.config.RsaKeyFileProperties;
import com.crazymaker.springcloud.base.service.JwtService;
import com.crazymaker.springcloud.base.service.impl.JwtServiceImpl;
import com.crazymaker.springcloud.standard.context.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Configuration
public class FilterConfig {
    @Resource
    private RsaKeyFileProperties rsaKeyFileProperties;

    //    @Bean
//    @Order(-1)
    public GlobalFilter userIdCheckGateWayFilter() {
        return new UserIdCheckGateWayFilter();
    }
// 第32章注释掉这个
//    @Bean
//    @Order(0)
//    public GlobalFilter authGlobalFilter() {
//        return new AuthGlobalFilter();
//    }


    @Bean
    @Order(0)
    public GlobalFilter pushMessageGlobalFilter() {
        return new PushMessageGlobalFilter();
    }


    @Bean
    @Order(0)
    public GlobalFilter httpBodyGlobalFilter() {
        return new HttpBodyGlobalFilter();
    }


}