package com.crazymaker.gateway.core.api.context;

import com.alibaba.fastjson.JSONObject;
import com.crazymaker.gateway.core.api.constant.ResponseCode;
import com.crazymaker.springcloud.common.util.JsonUtil;
import io.netty.handler.codec.http.*;
import lombok.Data;
import org.asynchttpclient.Response;

@Data
public class GatewayResponse {

    public static final String CODE = "code";

    public static final String STATUS = "status";

    public static final String DATA = "data";

    public static final String MESSAGE = "message";


    /**
     * 响应头
     */
    private HttpHeaders responseHeaders = new DefaultHttpHeaders();

    /**
     * 额外的响应结果
     */
    private final HttpHeaders extraResponseHeaders = new DefaultHttpHeaders();
    /**
     * 响应内容
     */
    private String content;

    /**
     * 异步返回对象
     */
    private Response futureResponse;

    /**
     * 响应返回码
     */
    private HttpResponseStatus httpResponseStatus;


    public GatewayResponse() {

    }

    /**
     * 设置响应头信息
     *
     * @param key
     * @param val
     */
    public void putHeader(CharSequence key, CharSequence val) {
        responseHeaders.add(key, val);
    }

    /**
     * 构建异步响应对象
     *
     * @param futureResponse
     * @return
     */
    public static GatewayResponse buildGatewayResponse(Response futureResponse) {
        GatewayResponse response = new GatewayResponse();
        response.setFutureResponse(futureResponse);
        response.setHttpResponseStatus(HttpResponseStatus.valueOf(futureResponse.getStatusCode()));
        return response;
    }

    /**
     * 处理返回json对象，失败时调用
     *
     * @param code
     * @param args
     * @return
     */
    public static GatewayResponse buildGatewayResponse(ResponseCode code, Object... args) {

        JSONObject jsonObject = JsonUtil.createJSONObject();
        jsonObject.put(STATUS, code.getStatus().code());
        jsonObject.put(CODE, code.getCode());
        jsonObject.put(MESSAGE, code.getMessage());

        GatewayResponse response = new GatewayResponse();
        response.setHttpResponseStatus(code.getStatus());
        response.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        response.setContent(JsonUtil.pojoToJson(jsonObject));

        return response;
    }

    /**
     * 处理返回json对象，成功时调用
     *
     * @param data
     * @return
     */
    public static GatewayResponse buildGatewayResponse(Object data) {

        JSONObject jsonObject = JsonUtil.createJSONObject();
        jsonObject.put(STATUS, ResponseCode.SUCCESS.getStatus().code());
        jsonObject.put(CODE, ResponseCode.SUCCESS.getCode());
        jsonObject.put(DATA, data);

        GatewayResponse response = new GatewayResponse();
        response.setHttpResponseStatus(ResponseCode.SUCCESS.getStatus());
        response.putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON + ";charset=utf-8");
        response.setContent(JsonUtil.pojoToJson(jsonObject));
        return response;
    }

}
