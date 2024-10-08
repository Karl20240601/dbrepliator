package com.crazymaker.springcloud.base.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.crazymaker.springcloud.base.service.JwtService;
import com.crazymaker.springcloud.common.constants.SessionConstants;
import com.crazymaker.springcloud.common.util.TimeUtil;
import com.crazymaker.springcloud.standard.context.SpringContextUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.crazymaker.springcloud.common.util.BeanUtil.toList;

/**
 * 认证授权相关工具类
 */
@Slf4j
@Data
public class AuthUtils {
    private static JwtService rsaJwtService;

    public static void setRsaJwtService(JwtService rsaJwtService) {
        AuthUtils.rsaJwtService = rsaJwtService;
    }

    private AuthUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static final String BASIC_ = "Basic ";

    /**
     * 获取requet(head/param)中的token
     *
     * @param request
     * @return
     */
    public static String extractToken(HttpServletRequest request) {
        String token = extractHeaderToken(request);
        if (token == null) {
            token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
            if (token == null) {
                log.debug("Token not found in request parameters.  Not an OAuth2 request.");
            }
        }
        return token;
    }

    public static List<RequestMatcher> patterns2Matchers(String... antPatterns) {
        List<RequestMatcher> matchers = new ArrayList<RequestMatcher>();
        for (String pattern : antPatterns) {
            matchers.add(new AntPathRequestMatcher(pattern, null));
        }
        return matchers;
    }


    public static List<String> patterns2List(String... antPatterns) {
        return toList(antPatterns);
    }

    /**
     * 解析head中的token
     *
     * @param request
     * @return
     */
    private static String extractHeaderToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(SessionConstants.TOKEN_HEADER);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE))) {
                String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }

    /**
     * *从header 请求中的clientId:clientSecret
     */
    public static String[] extractClient(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(BASIC_)) {
            throw new UnapprovedClientAuthenticationException("请求头中client信息为空");
        }
        return extractHeaderClient(header);
    }

    /**
     * 从header 请求中的clientId:clientSecret
     *
     * @param header header中的参数
     */
    public static String[] extractHeaderClient(String header) {
        byte[] base64Client = header.substring(BASIC_.length()).getBytes(StandardCharsets.UTF_8);
        byte[] decoded = Base64.getDecoder().decode(base64Client);
        String clientStr = new String(decoded, StandardCharsets.UTF_8);
        String[] clientArr = clientStr.split(":");
        if (clientArr.length != 2) {
            throw new RuntimeException("Invalid basic authentication token");
        }
        return clientArr;
    }

    /**
     * 获取登陆的用户名
     */
    public static String getUsername(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username = null;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = (String) principal;
        }
        return username;
    }


    /**
     * @param subject
     * @param salt    jwt header和jwt payload  加密用的盐
     * @return
     */
    public static String buildToken(String subject, String salt) {
        Algorithm algorithm = Algorithm.HMAC256(salt);
        //签发时间. 注意：尽量比当前时间稍微提前一点，防止验证时间相隔太短，导致验证不通过
        long start = TimeUtil.currentTimeMillis() - 60000;
        //过期时间. 在签发时间的基础上，加上一个时长
        Date end = new Date(start + SessionConstants.SESSION_TIME_OUT * 1000);  //设置过期
        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(new Date(start))
                .withExpiresAt(end)
                .sign(algorithm);

    }

    public static DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
    }

    public static String buildToken(String subject, String salt, Map<String, String> claims) {
        Algorithm algorithm = Algorithm.HMAC256(salt);
        //签发时间. 注意：尽量比当前时间稍微提前一点，防止验证时间相隔太短，导致验证不通过
        long start = TimeUtil.currentTimeMillis() - 60000;
        //过期时间. 在签发时间的基础上，加上一个时长
        Date end = new Date(start + SessionConstants.SESSION_TIME_OUT * 1000);  //设置过期


        JWTCreator.Builder builder = JWT.create();
        Iterator<Map.Entry<String, String>> it = claims.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> next = it.next();
            builder.withClaim(next.getKey(), next.getValue());

        }

        return builder.withSubject(subject)
                .withIssuedAt(new Date(start))
                .withExpiresAt(end)
                .sign(algorithm);


    }


    public static JwtService getRsaJwtService() {
        if (null == rsaJwtService) {
            rsaJwtService = SpringContextUtil.getBean(JwtService.class);
        }
        return rsaJwtService;
    }

    /**
     * @param uid       用户id
     * @param sessionId session di
     * @return
     */
    public static String buildRsaToken(String sessionId, String uid) {
        JwtService jwtService = getRsaJwtService();

        return jwtService.buildJwtRsaToken(sessionId, uid);
    }

    public static Payload<String> decodeRsaToken(String token) {
        JwtService jwtService = getRsaJwtService();

        return jwtService.decodeJwtRsaToken(token);
    }

    public static Payload<String> verifyJwtRsaToken(String token) {
        JwtService jwtService = getRsaJwtService();

        return jwtService.verifyJwtRsaToken(token);
    }

    /**
     * 时间是否过期
     *
     * @param issueAt 时间
     * @return 是否过期
     */
    public static boolean isOverdue(Date issueAt) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().isAfter(issueTime);
    }

}