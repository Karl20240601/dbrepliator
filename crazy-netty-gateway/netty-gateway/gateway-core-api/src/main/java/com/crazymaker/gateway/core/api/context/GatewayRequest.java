package com.crazymaker.gateway.core.api.context;

import com.crazymaker.gateway.core.api.constant.BasicConst;
import com.crazymaker.springcloud.common.util.TimeUtil;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Slf4j
public class GatewayRequest {


    private final long beginTime;

    private final Charset charset;


    private final String clientIp;

    /**
     * 请求的地址：IP：port
     */
    private final String host;

    /**
     * 请求的路径   /XXX/XXX/XX
     */
    private final String path;


    private final String uri;

    private final HttpMethod method;

    private final String contentType;


    private final HttpHeaders headers;


    //封装起来的原始的 http请求
    private final FullHttpRequest fullHttpRequest;


    private String body;


    private Map<String, io.netty.handler.codec.http.cookie.Cookie> cookieMap;

    private Map<String, List<String>> postParameters;


    /**
     * 可修改的Scheme，默认是http://
     */
    private String modifyScheme;

    private String modifyHost;

    private String modifyPath;

    private final QueryStringDecoder queryDecoder;

    public GatewayRequest(Charset charset, String clientIp, String host, String uri, HttpMethod method, String contentType, HttpHeaders headers, FullHttpRequest fullHttpRequest) {
        this.beginTime = TimeUtil.currentTimeMillis();
        this.charset = charset;
        this.clientIp = clientIp;
        this.host = host;
        this.uri = uri;
        this.method = method;
        this.contentType = contentType;
        this.headers = headers;
        this.fullHttpRequest = fullHttpRequest;
        this.modifyHost = host;
        this.queryDecoder = new QueryStringDecoder(uri, charset);
        this.path = queryDecoder.path();
        this.modifyPath = path;


        this.modifyScheme = BasicConst.HTTP_PREFIX_SEPARATOR;


    }

    /**
     * 获取请求体
     *
     * @return
     */
    public String getBody() {
        if (StringUtils.isEmpty(body)) {
            body = fullHttpRequest.content().toString(charset);
        }
        return body;
    }


    public String getHeader(String name) {

        if(headers==null)return null;
        return  headers.get(name);
    }
    public Cookie getCookie(String name) {
        if (cookieMap == null) {
            cookieMap = new HashMap<String, io.netty.handler.codec.http.cookie.Cookie>();
            String cookieStr = getHeaders().get(HttpHeaderNames.COOKIE);
            Set<io.netty.handler.codec.http.cookie.Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieStr);
            for (io.netty.handler.codec.http.cookie.Cookie cookie : cookies) {
                cookieMap.put(name, cookie);
            }
        }
        return cookieMap.get(name);
    }


    public String getFinalUrl() {
        return modifyScheme + modifyHost + modifyPath;
    }
}
