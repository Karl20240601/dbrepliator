package com.crazymaker.gateway.filter.safety;

import com.crazymaker.gateway.core.api.constant.ResponseCode;
import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.error.ResponseException;
import com.crazymaker.gateway.core.api.filterchain.DefaultLinkedFilter;
import com.crazymaker.gateway.core.api.filterchain.FilterAnnotation;
import com.crazymaker.springcloud.base.auth.AuthUtils;
import com.crazymaker.springcloud.base.auth.Payload;
import com.crazymaker.springcloud.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import static com.crazymaker.gateway.core.api.constant.BasicConst.ROUTER_FILTER_ORDER;
import static com.crazymaker.gateway.core.api.constant.BasicConst.USER_AUTH_RULE_CONFIG;
import static com.crazymaker.gateway.core.api.filterchain.ProcessorFilterType.PRE;


@Slf4j
@FilterAnnotation(id = "TokenAuthFilter",
        name = "",
        type = PRE,
        order = ROUTER_FILTER_ORDER - 1000)
public class TokenAuthFilter   extends DefaultLinkedFilter<GatewayContext> {
    private static final String TOKEN = "token";

    @Override
    public void doFilter(GatewayContext context, Object... args) throws Throwable {
        //检查是否需要用户鉴权
        if (context.getRule().getFilterParam(USER_AUTH_RULE_CONFIG) == null) {
            return;
        }

        String token = context.getRequest().getHeader(TOKEN);
        if (StringUtils.isBlank(token)) {
            new ResponseException(ResponseCode.UNAUTHORIZED);
        }
        Payload<String> jwt = AuthUtils.decodeRsaToken(token);
        boolean overdue = AuthUtils.isOverdue(jwt.getExpiration());

        if (overdue) {
            throw BusinessException.builder().errMsg("token已经过期,请重新登录").build();
        }

        String userId = jwt.getId();
        context.getRequest().getHeaders().add("user-id",userId);

        super.fireNext(context);

    }

}
