package com.crazymaker.cloud.nacos.demo.gateway.filter;

import com.crazymaker.springcloud.base.auth.AuthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.List;

//@Component
@Slf4j
public class HttpBodyGlobalFilter implements GlobalFilter, Ordered {
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    List<String> whiteUrls = AuthUtils.patterns2List(
            "/**/api/crazymaker/message/push/v1",
            "/**/api/crazymaker/simple/send/v1"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();
        //白名单去解析body
        Iterator<String> it = whiteUrls.iterator();
        while (it.hasNext()) {
            String next = it.next();

//            antPathMatcher.match("/api/**/auth/**", path)
            if (antPathMatcher.match(next, path)) {

                /**
                 * save request path and serviceId into gateway context
                 */
                HttpHeaders headers = request.getHeaders();

                // 处理参数
                MediaType contentType = headers.getContentType();
                long contentLength = headers.getContentLength();
                if (contentLength > 0) {
                    if (MediaType.APPLICATION_JSON.equals(contentType) || MediaType.APPLICATION_JSON_UTF8.equals(contentType)) {
                        return readBody(exchange, chain);
                    }
                }
            }
        }
        return chain.filter(exchange);


    }


    /**
     * default HttpMessageReader
     */
    private static final List<HttpMessageReader<?>> messageReaders =
            HandlerStrategies.withDefaults().messageReaders();

    /**
     * ReadJsonBody
     *
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain) {
        /**
         * join the body
         */
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            DataBufferUtils.release(dataBuffer);
            Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                DataBufferUtils.retain(buffer);
                return Mono.just(buffer);
            });
            /**
             * repackage ServerHttpRequest
             */
            ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return cachedFlux;
                }
            };
            /**
             * mutate exchage with new ServerHttpRequest
             */
            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
            /**
             * read body string with default messageReaders
             */
            return ServerRequest.create(mutatedExchange, messageReaders).bodyToMono(String.class)
                    .doOnNext(objectValue -> {
                        log.debug("[GatewayContext]Read JsonBody:{}", objectValue);
                    }).then(chain.filter(mutatedExchange));
        });
    }

    //值越大，优先级越低
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1000 + 1000;
    }
}
