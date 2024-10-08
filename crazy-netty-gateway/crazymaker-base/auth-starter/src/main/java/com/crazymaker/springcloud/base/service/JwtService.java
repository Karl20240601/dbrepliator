package com.crazymaker.springcloud.base.service;

import com.crazymaker.springcloud.base.auth.Payload;

public interface JwtService {

    /**
     * 私钥加密token
     *
     * @param sessionId 载荷中的sessionId
     * @param uid       用户id
     * @return String
     */
    public String buildJwtRsaToken(String sessionId, String uid);

    /**
     * 获取token中的用户信息
     *
     * @param token 用户请求中的令牌
     * @return 用户信息
     */
    public Payload<String> decodeJwtRsaToken(String token);

    public Payload<String> verifyJwtRsaToken(String token);

}
