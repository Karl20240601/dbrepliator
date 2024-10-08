package com.crazymaker.cloud.nacos.demo.gateway.filter;

import com.crazymaker.springcloud.base.auth.AuthUtils;
import com.crazymaker.springcloud.base.auth.Payload;
import com.crazymaker.springcloud.common.constants.SessionConstants;
import com.crazymaker.springcloud.common.result.RestOut;
import com.crazymaker.springcloud.common.util.JsonUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.crazymaker.springcloud.common.constants.PushConstants.REQUEST_REQUEST_START_TIME;

public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    List<String> whiteUrls = AuthUtils.patterns2List(
            "/**/api/auth/app/token/v1",
            "/**/api/session/login/v1",
            "/**/api/user/login/v1",
            "/**/v2/api-docs",
            "/**/swagger-resources/configuration/ui",
            "/**/api/user/passwordEncoder/v1",
            "/**/swagger-resources",
            "/**/swagger-resources/configuration/security",
//                "/api/user/say/hello/v1",
//                "/api/user/add/v1",
//                "/api/user/speed/test/v1",
//                "/api/user/*/detail/v1",
            "/**/images/**",
            "/**/swagger-ui.html",
            "/**/webjars/**",
            "/**/favicon.ico",
            "/**/css/**",
            "/blog1/**",
            "/**/js/**");


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        //白名单直接过
        Iterator<String> it = whiteUrls.iterator();
        while (it.hasNext()) {
            String pattern = it.next();

//            antPathMatcher.match("/api/**/auth/**", path)
            if (antPathMatcher.match(pattern, path)) {
                return chain.filter(exchange);

            }
        }

        ServerHttpResponse response = exchange.getResponse();
        List<String> tokenList = request.getHeaders().get(SessionConstants.AUTHORIZATION_HEAD);
        if (null == tokenList) {
            return illegalAccess(response);
        }

        try {
//            Payload<String> payload = AuthUtils.decodeRsaToken(tokenList.get(0));
            Payload<String> payload = AuthUtils.verifyJwtRsaToken(tokenList.get(0));
            // 重置请求头参数
            ServerHttpRequest newRequest = exchange.getRequest().mutate().headers(httpHeaders -> {

                httpHeaders.remove(SessionConstants.USER_IDENTIFIER);
                httpHeaders.add(SessionConstants.USER_IDENTIFIER, payload.getId());

                if (null != payload.getUserInfo()) {
                    httpHeaders.remove(SessionConstants.SESSION_IDENTIFIER);
                    httpHeaders.add(SessionConstants.SESSION_IDENTIFIER, payload.getUserInfo());
                }
            }).build();

            ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
            newExchange.getAttributes().put(REQUEST_REQUEST_START_TIME, new Date());

            return chain.filter(newExchange);
        } catch (Exception e) {
            return illegalAccess(response);
        }

    }

    @Override
    public int getOrder() {

        return HIGHEST_PRECEDENCE + 1000;
    }


    private Mono<Void> illegalAccess(ServerHttpResponse serverHttpResponse) {

        //指定编码，否则在浏览器中会中文乱码
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        RestOut<String> stringMasResponse = RestOut.error("没有访问权限，请登陆或者联系管理员！");
        byte[] jsonBytes = JsonUtil.object2JsonBytes(stringMasResponse);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(jsonBytes);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

}
