package com.crazymaker.springcloud.base.security.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenClearLogoutHandler implements LogoutHandler {


    public TokenClearLogoutHandler() {
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        clearToken(authentication);
    }

    protected void clearToken(Authentication authentication) {
        if (authentication == null) {
            return;
        }
        UserDetails user = (UserDetails) authentication.getPrincipal();
        if (null == user) {
            return;
        }

        /*JwtAuthenticationToken jwtAuthenticationToken =
                (JwtAuthenticationToken) authentication;
        DecodedJWT token = jwtAuthenticationToken.getDecodedJWT();*/
    }

}
