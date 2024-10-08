package com.crazymaker.gateway.filterchain;

import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.filterchain.DefaultLinkedFilter;
import com.crazymaker.gateway.filter.HeadFilter;

/**
 * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 * @一句话介绍： 默认的链表实现类
 * @date
 * @详细介绍： 1.
 * 2.
 */
public class DefaultFilterChain extends DefaultLinkedFilter<GatewayContext> {

    private final String id;

    public DefaultFilterChain(String id) {
        this.id = id;
    }

    /**
     * 头结点
     */
    DefaultLinkedFilter<GatewayContext> head = new HeadFilter();

    /**
     * 尾节点
     */
    DefaultLinkedFilter<GatewayContext> end = head;


    @Override
    public void addLast(DefaultLinkedFilter<GatewayContext> filter) {
        end.setNext(filter);
        end = filter;
    }

    @Override
    public void setNext(DefaultLinkedFilter<GatewayContext> filter) {
        addLast(filter);
    }

    @Override
    public DefaultLinkedFilter<GatewayContext> getNext() {
        return head.getNext();
    }

     //执行整个责任链
    @Override
    public void doFilter(GatewayContext ctx, Object... args) throws Throwable {
        //进入头部：
        // 这里就是真正开始 责任链 操作
        head.doFilter(ctx, args);
    }

    public String getId() {
        return id;
    }

}
