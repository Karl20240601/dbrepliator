package com.crazymaker.gateway.filter;

import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.filterchain.DefaultLinkedFilter;

/**
 * @ClassName HeadFilter
 * @Description TODO
 * @Author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 */

public class HeadFilter extends DefaultLinkedFilter<GatewayContext> {


    @Override
    public void doFilter(GatewayContext ctx, Object... args) throws Throwable {
        super.fireNext(ctx, args);
    }


}
