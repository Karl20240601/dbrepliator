package com.crazymaker.cloud.nacos.demo.gateway.filter;

import cn.hutool.core.date.DateUtil;
import com.crazymaker.cloud.nacos.demo.gateway.service.RocketmqMessageRelayService;
import com.crazymaker.springcloud.base.auth.AuthUtils;
import com.crazymaker.springcloud.common.constants.SessionConstants;
import com.crazymaker.springcloud.common.result.RestOut;
import com.crazymaker.springcloud.common.util.JsonUtil;
import com.crazymaker.springcloud.standard.context.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static com.crazymaker.cloud.nacos.demo.gateway.service.RocketmqMessageRelayService.SIZE_LIMIT;
import static com.crazymaker.springcloud.common.constants.PushConstants.REQUEST_REQUEST_START_TIME;

@Slf4j
public class PushMessageGlobalFilter implements GlobalFilter, Ordered {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    private RocketmqMessageRelayService rocketmqMessageRelayService;


    List<String> pushUrls = AuthUtils.patterns2List(
            "/**/api/crazymaker/message/push/v1",
            "/**/api/crazymaker/simple/send/v1"
    );

    /**
     * 临时放 token
     * <p>
     * <p>
     * {
     * "appId": 0,
     * "appKey": "",
     * "body": "巴巴版本不不bbbbbbbbbbbbbb",
     * "msgId": 1234553321,
     * "targetIds": "1860000000",
     * "targetType": 1,
     * "title": "吞吞吐吐ttttt"
     * }
     *
     * <p>
     * eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiIxIiwic2lkIjoiOTE5YjEwYWQtMDkwZS00ODFhLWIyZGQtMTgxMGRhZDA4ZjUwIiwiZXhwIjoxNjQ3OTQ5NTExLCJpYXQiOjE2NDc5MTcxMTF9.I2ZjhZNEqOD4-cFzBi19jiiKTWo8iV2maSX4MLek70dBil6W8GKxjdpHVTNsXyjeduU4MeEZVkN4tqiYYXXfkmalKjuws4QpCSilOK4QmE1QCiLpt138jqUZAIGD__W2FBWxDF5bL5ffgqrbIKtrRlsufwQQmf-xuoc1sci2zxhp4iKJ9GhdXp1tHuDB9L-TCo_gh7nMuKvTxJmg49JTbBig3VGBU2nMYZ87OpY79ymU0T_DtDojFXRykiyeNrFUCVh5HgfYAZV-KdMNvXdMz1Q43J1fWQdcXkAL_gxBSF_bkhbAf3G3VqxS9QVsnor9RDnrCJzsHQmlnM17KZBiCQ
     */

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        //白名单直接过
        Iterator<String> it = pushUrls.iterator();
        while (it.hasNext()) {
            String next = it.next();

//            antPathMatcher.match("/api/**/auth/**", path)
            if (antPathMatcher.match(next, path)) {

                String userId = request.getHeaders().getFirst(SessionConstants.USER_IDENTIFIER);
                ServerHttpResponse response = exchange.getResponse();


                RecorderServerHttpRequestDecorator requestDecorator = new RecorderServerHttpRequestDecorator(request);
                Flux<DataBuffer> body = requestDecorator.getBody();

//                AtomicReference<String> requestBody = new AtomicReference<>("");

//                body.subscribe(buffer -> {
//                    CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
//                    requestBody.set(charBuffer.toString());
//                });
//                //获取body参数
//                JSONObject requestParams = JSONObject.parseObject(requestBody.get());


                AtomicReference<byte[]> requestBody = new AtomicReference<>(new byte[]{});

                body.subscribe(buffer -> {
                    requestBody.set(buffer.asByteBuffer().array());
                });

                if (requestBody.get().length > SIZE_LIMIT) {
                    return tooBigMessage(response);

                }

                Long id = getRocketmqMessageRelayService().putMessage(requestBody.get(), userId);

                if (null == id) {
                    return failure(response);

                }


                return success(response);

            }
        }
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Date start_time = exchange.getAttribute(REQUEST_REQUEST_START_TIME);
            if (null != start_time) {
                long castTime = DateUtil.betweenMs(start_time, new Date());
                log.info(" this send cast time is {}", castTime);
            }
        }));


    }

    //值越大，优先级越低

    @Override
    public int getOrder() {
        //比验证的大，这是越大越低的模式
        return HIGHEST_PRECEDENCE + 1000 + 1000 + 1000;
    }


    private Mono<Void> failure(ServerHttpResponse serverHttpResponse) {

        //指定编码，否则在浏览器中会中文乱码
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        RestOut<String> stringMasResponse = RestOut.error("发送失败！！！");
        byte[] jsonBytes = JsonUtil.object2JsonBytes(stringMasResponse);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(jsonBytes);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    private Mono<Void> tooBigMessage(ServerHttpResponse serverHttpResponse) {

        //指定编码，否则在浏览器中会中文乱码
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        RestOut<String> stringMasResponse = RestOut.error("消息太大了，超过了4M！");
        byte[] jsonBytes = JsonUtil.object2JsonBytes(stringMasResponse);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(jsonBytes);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    private Mono<Void> success(ServerHttpResponse serverHttpResponse) {

        //指定编码，否则在浏览器中会中文乱码
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        RestOut<String> stringMasResponse = RestOut.succeed("发送成功！");
        byte[] jsonBytes = JsonUtil.object2JsonBytes(stringMasResponse);
        DataBuffer buffer = serverHttpResponse.bufferFactory().wrap(jsonBytes);
        return serverHttpResponse.writeWith(Mono.just(buffer));
    }

    /**
     * default HttpMessageReader
     */
    private static final List<HttpMessageReader<?>> messageReaders =
            HandlerStrategies.withDefaults().messageReaders();


    public class RecorderServerHttpRequestDecorator extends ServerHttpRequestDecorator {
        private final List<DataBuffer> dataBuffers = new ArrayList<>();

        public RecorderServerHttpRequestDecorator(ServerHttpRequest delegate) {
            super(delegate);
            super.getBody().map(dataBuffer -> {
                dataBuffers.add(dataBuffer);
                return dataBuffer;
            }).subscribe();
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return Flux.fromIterable(dataBuffers)
                    .map(buf -> buf.factory().wrap(buf.asByteBuffer()));

        }

    }

    public RocketmqMessageRelayService getRocketmqMessageRelayService() {
        if (null == rocketmqMessageRelayService) {
            rocketmqMessageRelayService = SpringContextUtil.getBean(RocketmqMessageRelayService.class);
        }

        return rocketmqMessageRelayService;

    }
}
