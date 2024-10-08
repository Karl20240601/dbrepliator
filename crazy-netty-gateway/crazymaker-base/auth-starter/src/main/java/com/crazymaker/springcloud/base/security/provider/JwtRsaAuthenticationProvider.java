package com.crazymaker.springcloud.base.security.provider;

import com.crazymaker.springcloud.base.auth.Payload;
import com.crazymaker.springcloud.base.security.token.JwtRsaAuthenticationToken;
import com.crazymaker.springcloud.common.constants.SessionConstants;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtRsaAuthenticationProvider implements AuthenticationProvider {

//    private RedisOperationsSessionRepository sessionRepository;

//    public JwtRsaAuthenticationProvider(RedisOperationsSessionRepository sessionRepository)
//    {
//        this.sessionRepository = sessionRepository;
//    }

    public JwtRsaAuthenticationProvider() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtRsaAuthenticationToken realAuthentication = (JwtRsaAuthenticationToken) authentication;
        Payload<String> decodedJWT = realAuthentication.getDecodedJWT();

        //判断JWT令牌是否过期
//        DecodedJWT decodedJWT = ((JwtAuthenticationToken) authentication).getDecodedJWT();
//        if (decodedJWT.getExpiration().before(Calendar.getInstance().getTime())) {
//            throw new NonceExpiredException("认证过期");
//        }
//

        //取得 session id
        String sid = decodedJWT.getUserInfo();

        //是用户下面的 账号，不需要啥会话，
        // 只需要简单验证
        if (null == sid) {
            realAuthentication.setDetails(null);
            realAuthentication.setAuthenticated(true);
            return realAuthentication;
        }


        //取得 uid
        String uid = decodedJWT.getId();

        //取得令牌字符串，用于验证是否重复登录
        String newToken = decodedJWT.getToken();


        //sid-> username
        //uid-> password
        UserDetails userDetails = User.builder()
                .username(uid)
                .password(sid)
                .authorities(SessionConstants.USER_INFO)
                .build();


        //返回认证通过的token，包含用户的 id 等信息
        realAuthentication.setDetails(userDetails);
        realAuthentication.setAuthenticated(true);

//        JwtRsaAuthenticationToken passedToken =
//                new JwtRsaAuthenticationToken(userDetails, decodedJWT, userDetails.getAuthorities());
//        passedToken.setAuthenticated(true);


        return realAuthentication;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtRsaAuthenticationToken.class);
    }

}
