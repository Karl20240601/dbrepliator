package com.crazymaker.springcloud.base.security.token;

import com.crazymaker.springcloud.base.auth.Payload;
import com.crazymaker.springcloud.common.util.TimeUtil;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static com.crazymaker.springcloud.common.constants.SessionConstants.DEFAULT_SCOPE;

public class JwtRsaAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 3981518947978158945L;

    //用户信息: 用户id 、密码
    private UserDetails userDetails;
    // 封装的 JWT 认证信息
    private Payload<String> decodedJWT;

    public JwtRsaAuthenticationToken(Payload<String> jwt) {
        super(Collections.emptyList());
        this.decodedJWT = jwt;
    }

    public JwtRsaAuthenticationToken(UserDetails userDetails,
                                     Payload<String> jwt,
                                     Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userDetails = userDetails;
        this.decodedJWT = jwt;
    }

    @Override
    public void setDetails(Object details) {
        super.setDetails(details);
        this.setAuthenticated(true);
    }

    public Payload<String> getDecodedJWT() {
        return decodedJWT;
    }

    @Override
    public Object getCredentials() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return decodedJWT.getUserInfo();
    }


    public OAuth2AccessToken createAccessToken() {
        DefaultOAuth2AccessToken auth2Token = new DefaultOAuth2AccessToken(decodedJWT.getId());

        long validitySeconds = decodedJWT.getExpiration().getTime() - TimeUtil.currentTimeMillis();
        auth2Token.setExpiration(new Date(TimeUtil.currentTimeMillis() + validitySeconds));


        auth2Token.setRefreshToken(null);
        auth2Token.setScope(Collections.singleton(DEFAULT_SCOPE));
        return auth2Token;
    }
}
