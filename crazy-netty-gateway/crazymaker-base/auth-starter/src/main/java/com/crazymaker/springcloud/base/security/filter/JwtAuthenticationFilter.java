package com.crazymaker.springcloud.base.security.filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.crazymaker.springcloud.base.auth.AuthUtils;
import com.crazymaker.springcloud.base.auth.Payload;
import com.crazymaker.springcloud.base.security.exception.CustomAuthenticationException;
import com.crazymaker.springcloud.base.security.token.JwtRsaAuthenticationToken;
import com.crazymaker.springcloud.base.service.JwtService;
import com.crazymaker.springcloud.common.constants.SessionConstants;
import com.crazymaker.springcloud.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private RequestMatcher requiresAuthenticationRequestMatcher;
    private List<RequestMatcher> permissiveRequestMatchers;
    private AuthenticationManager authenticationManager;

    private JwtService jwtService;
    private AuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

    public JwtAuthenticationFilter() {
        this.requiresAuthenticationRequestMatcher = new RequestHeaderRequestMatcher(SessionConstants.AUTHORIZATION_HEAD);
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
        Assert.notNull(successHandler, "AuthenticationSuccessHandler must be specified");
        Assert.notNull(failureHandler, "AuthenticationFailureHandler must be specified");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        /**
         * 处理掉不需要过滤的
         */
        if (!requiresAuthentication(request, response)) {

            filterChain.doFilter(request, response);
            return;
        }

        Authentication passedToken = null;
        AuthenticationException failed = null;

        //从HTTP请求取得 JWT 令牌的头部字段
        String token = null;

        token = request.getHeader(SessionConstants.AUTHORIZATION_HEAD);


        //没有拿到头部，报异常
        if (StringUtils.isEmpty(token)) {
            failed = new InsufficientAuthenticationException("请求头认证消息为空");
            unsuccessfulAuthentication(request, response, failed);
            return;
        }
        token = StringUtils.removeStart(token, "Bearer ");
        try {

            if (StringUtils.isNotBlank(token)) {

                //token 解码
//                JwtAuthenticationToken authToken = new JwtAuthenticationToken( AuthUtils.decodeToken(token));

//                Payload<String> payload = AuthUtils.decodeRsaToken(token);
                Payload<String> payload = AuthUtils.verifyJwtRsaToken(token);

                //如果没有抛出异常，说明解码通过

                JwtRsaAuthenticationToken authToken = new JwtRsaAuthenticationToken(payload);
                //passedToken 实际上还是 authToken
                passedToken = this.getAuthenticationManager().authenticate(authToken);
                UserDetails details = (UserDetails) passedToken.getDetails();

                if (null != details) {
                    //password -> uid
                    request.setAttribute(SessionConstants.USER_IDENTIFIER, details.getUsername());
                    //getUsername-> sid
                    request.setAttribute(SessionConstants.SESSION_IDENTIFIER, payload.getUserInfo());
//                    request.setAttribute(SessionConstants.SESSION_IDENTIFIER, details.getPassword());
                }
            } else {
                failed = new InsufficientAuthenticationException("请求头认证消息为空");

            }
        } catch (JWTDecodeException e) {
            logger.error("JWT format error", e);
            failed = new InsufficientAuthenticationException("请求头认证消息格式错误", failed);
        } catch (InternalAuthenticationServiceException e) {
            logger.error(
                    "An internal error occurred while trying to authenticate the user.",
                    failed);
            failed = e;
        } catch (AuthenticationException e) {
            // Authentication failed
            failed = e;
        } catch (BusinessException e) {
            // Authentication failed
            failed = new CustomAuthenticationException("token 校验异常");
        } catch (Exception e) {
            // Authentication failed
            failed = new CustomAuthenticationException("token 校验异常");
        }
        if (passedToken != null) {
            successfulAuthentication(request, response, filterChain, passedToken);
        } else if (!permissiveRequest(request)) {
            unsuccessfulAuthentication(request, response, failed);
            return;
        }

        filterChain.doFilter(request, response);
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    protected AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    protected boolean requiresAuthentication(HttpServletRequest request,
                                             HttpServletResponse response) {

        return requiresAuthenticationRequestMatcher.matches(request);

    }

    protected boolean permissiveRequest(HttpServletRequest request) {
        if (permissiveRequestMatchers == null)
            return false;
        for (RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
            if (permissiveMatcher.matches(request))
                return true;
        }
        return false;
    }

    public void setPermissiveUrl(String... urls) {
        if (permissiveRequestMatchers == null)
            permissiveRequestMatchers = new ArrayList<>();
        for (String url : urls)
            permissiveRequestMatchers.add(new AntPathRequestMatcher(url));
    }

    public void setAuthenticationSuccessHandler(
            AuthenticationSuccessHandler successHandler) {
        Assert.notNull(successHandler, "successHandler cannot be null");
        this.successHandler = successHandler;
    }

    public void setAuthenticationFailureHandler(
            AuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler cannot be null");
        this.failureHandler = failureHandler;
    }

    protected AuthenticationSuccessHandler getSuccessHandler() {
        return successHandler;
    }

    protected AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }

}
