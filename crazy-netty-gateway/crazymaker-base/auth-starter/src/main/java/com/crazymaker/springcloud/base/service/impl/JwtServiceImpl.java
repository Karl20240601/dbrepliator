package com.crazymaker.springcloud.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.crazymaker.springcloud.base.auth.Payload;
import com.crazymaker.springcloud.base.auth.RsaUtils;
import com.crazymaker.springcloud.base.config.RsaKeyFileProperties;
import com.crazymaker.springcloud.base.service.JwtService;
import com.crazymaker.springcloud.common.constants.SessionConstants;
import com.crazymaker.springcloud.common.exception.BusinessException;
import com.crazymaker.springcloud.common.util.TimeUtil;
import com.crazymaker.springcloud.common.util.UUIDUtil;
import io.jsonwebtoken.*;
import lombok.Setter;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 生成token以及校验token相关方法
 */

@Setter
public class JwtServiceImpl implements JwtService {

    private static final String JWT_PAYLOAD_SID = "sid";
    private static Map<PublicKey, JwtParser> publicParserMap = new ConcurrentHashMap<>();
    private static Map<PrivateKey, JwtParser> privateParserMap = new ConcurrentHashMap<>();
    private final RsaKeyFileProperties rsaKeyFileProperties;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private volatile RsaVerifier rsaPubVerifier;

    public JwtServiceImpl(RsaKeyFileProperties rsaKeyFileProperties) {
        this.rsaKeyFileProperties = rsaKeyFileProperties;
    }

    /**
     * 私钥加密token
     *
     * @param uid        载荷中的数据
     * @param privateKey 私钥
     * @param expire     过期时间，单位分钟
     * @return JWT
     */
    public static String buildJwtRsaToken(Long uid, PrivateKey privateKey, int expire) {

        //签发时间. 注意：尽量比当前时间稍微提前一点，防止验证时间相隔太短，导致验证不通过
        long startTime = TimeUtil.currentTimeMillis() - 60000;
        //过期时间. 在签发时间的基础上，加上一个时长
        Date end = new Date(startTime + SessionConstants.SESSION_TIME_OUT * 1000);  //设置过期
        Date start = new Date(startTime);  //设开始
        return Jwts.builder()
                .claim(JWT_PAYLOAD_SID, createJTI())
                .setId(String.valueOf(uid))
                .setExpiration(end)
                .setIssuedAt(start)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

    }


    private static JwtParser getPublicParser(PublicKey publicKey) {
        JwtParser publicParser = publicParserMap.get(publicKey);
        if (null == publicParser) {
            publicParser = Jwts.parser().setSigningKey(publicKey);
            publicParserMap.put(publicKey, publicParser);
        }
        return publicParser;
    }

    private static JwtParser getPrivateParser(PrivateKey privateKey) {
        JwtParser jwtParser = privateParserMap.get(privateKey);
        if (null == jwtParser) {
            jwtParser = Jwts.parser().setSigningKey(privateKey);
            privateParserMap.put(privateKey, jwtParser);
        }
        return jwtParser;
    }

    private static String createJTI() {
        return UUIDUtil.createJTI();
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static Payload<String> decodeJwtRsaToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parseJwtRsaToken(token, publicKey);
        Claims claims = claimsJws.getBody();
        Payload<String> payload = new Payload<>();
        payload.setId(claims.getId());

        if (null != claims.get(JWT_PAYLOAD_SID)) {
            payload.setUserInfo(claims.get(JWT_PAYLOAD_SID).toString());
        } else {
            payload.setUserInfo(null);

        }
        payload.setExpiration(claims.getExpiration());
        return payload;
    }


    /**
     * 私钥加密token
     *
     * @param sessionId 载荷中的sessionId
     * @param userId    用户id/app id
     * @return String
     */
    public String buildJwtRsaToken(String sessionId, String userId) {

        //签发时间. 注意：尽量比当前时间稍微提前一点，防止验证时间相隔太短，导致验证不通过
        long startTime = TimeUtil.currentTimeMillis() - 60000;
        //过期时间. 在签发时间的基础上，加上一个时长
        Date end = new Date(startTime + SessionConstants.SESSION_TIME_OUT * 1000);  //设置过期
        Date start = new Date(startTime);  //设开始
        JwtBuilder builder = Jwts.builder()
                .setId(userId);

        if (null != sessionId) {
            builder.claim(JWT_PAYLOAD_SID, sessionId);

        }
        builder.setExpiration(end)
                .setIssuedAt(start)
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256);

        return builder.compact();
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return Jws<Claims>
     */
    private static Jws<Claims> parseJwtRsaToken(String token, PublicKey publicKey) {
        try {
            JwtParser publicParser = getPublicParser(publicKey);
            return publicParser.parseClaimsJws(token);

        } catch (Exception e) {
            e.printStackTrace();
            throw BusinessException.builder().errMsg("token 验证异常").build();
        }
    }

    /**
     * 获取token中的用户信息
     *
     * @param token 用户请求中的令牌
     * @return 用户信息
     */
    public Payload<String> decodeJwtRsaToken(String token) {

        Jws<Claims> claimsJws = parseJwtRsaToken(token, getPublicKey());
        Claims body = claimsJws.getBody();
        Payload<String> payload = new Payload<>();
        payload.setId(body.getId());
        if (null != body.get(JWT_PAYLOAD_SID)) {
            payload.setUserInfo(body.get(JWT_PAYLOAD_SID).toString());
        } else {
            payload.setUserInfo(null);

        }
        payload.setExpiration(body.getExpiration());

        payload.setToken(token);
        return payload;
    }

    public Payload<String> verifyJwtRsaToken(String token) {
        SignatureVerifier rsaVerifier = getRsaPubVerifier();
        Jwt jwt = JwtHelper.decodeAndVerify(token, rsaVerifier);

        JSONObject body = JSONObject.parseObject(jwt.getClaims());
        Payload<String> payload = new Payload<>();
        payload.setId(String.valueOf(body.get("jti")));
        if (null != body.get(JWT_PAYLOAD_SID)) {
            payload.setUserInfo(body.get(JWT_PAYLOAD_SID).toString());
        } else {
            payload.setUserInfo(null);

        }
//        payload.setExpiration(String.valueOf(body.get("exp")));

        payload.setToken(token);
        return payload;
    }

    /**
     * {"exp":1563256084,"user_name":"admin","authorities":["ADMIN"],"jti":"4ce02f54-3d1c-4461-8af1-73f0841a35df","client_id":"webApp","scope":["app"]}
     *
     * @param token token值
     * @return
     */


    public JSONObject verifyThenDecode2Json(String token) {
        SignatureVerifier rsaVerifier = getRsaPubVerifier();
        Jwt jwt = JwtHelper.decodeAndVerify(token, rsaVerifier);
        return JSONObject.parseObject(jwt.getClaims());
    }

    private SignatureVerifier getRsaPubVerifier() {
        if (null == rsaPubVerifier) {
            rsaPubVerifier = new RsaVerifier((RSAPublicKey) getPublicKey());

        }
        return rsaPubVerifier;
    }

    private PrivateKey getPrivateKey() {
        if (null == privateKey) {
            String file = rsaKeyFileProperties.getPrivateKeyFile();
            try {
                privateKey = RsaUtils.getPrivateKey(file);
            } catch (Exception e) {
                e.printStackTrace();
                throw BusinessException.builder().errMsg("生成私钥报错").build();
            }

        }
        return privateKey;
    }

    private PublicKey getPublicKey() {
        if (null == publicKey) {
            String file = rsaKeyFileProperties.getPublicKeyFile();
            try {
                publicKey = RsaUtils.getPublicKey(file);
            } catch (Exception e) {
                e.printStackTrace();
                throw BusinessException.builder().errMsg("生成公钥报错").build();
            }

        }
        return publicKey;
    }

}
