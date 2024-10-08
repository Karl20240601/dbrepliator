package com.crazymaker.springcloud.test.sso;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.crazymaker.springcloud.base.auth.AuthUtils;
import com.crazymaker.springcloud.common.util.JsonUtil;
import com.crazymaker.springcloud.common.util.UUIDUtil;
import org.junit.Test;


public class AuthUtilTest {


    private String sid = UUIDUtil.uuid();
    private String uid = UUIDUtil.uuid();
    private String salt = "123456";

    @Test
    public void testGenToken() throws Exception {

        // 构建JWT token
        String token = AuthUtils.buildToken(sid, salt);
        System.out.println("token = " + token);
    }


    @Test
    public void testdecodeToken() throws Exception {
        //如果token格式正确，就验证token

        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYmFhNmE4OGY5Njg0MjdhYmZhMjVkNTMzZDgxMzM0ZCIsImV4cCI6MTY0NTM0ODgwNywiaWF0IjoxNjQ1MzE2NDA3fQ.xFCTRZ2XyZ26zDqztD9ljnb2vFp4b6ixgzUFn7-UP9g";

        DecodedJWT jwt = AuthUtils.decodeToken(token);

        String json = JsonUtil.pojoToJson(jwt);

        System.err.println(json);
    }

    @Test
    public void testGenRsaToken() throws Exception {

        // 构建JWT token

        String token = AuthUtils.buildRsaToken(sid, uid);
        System.out.println("token = " + token);
    }
}