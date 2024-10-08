package com.crazymaker.gateway.core.api.filterchain;

import com.crazymaker.gateway.core.api.context.GatewayContext;
import com.crazymaker.gateway.core.api.util.ResponseHelper;

/**
 * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 * @一句话介绍： 基础的过滤器
 * @date
 * @详细介绍： 1.
 * 2.
 */
public class DefaultLinkedFilter<T> implements Filter<GatewayContext> {

    //	做一个链表里面的一个元素，必须要有下一个元素的引用
    protected DefaultLinkedFilter<T> next = null;
    protected boolean global = true;

    /**
     * @一句话介绍： 真正执行过滤器的方法
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @date
     * @详细介绍： 1.
     * 2.
     */
    @Override
    public void doFilter(GatewayContext gatewayContext, Object... args) throws Throwable {

    }

    @Override
    public void fireNext(GatewayContext ctx, Object... args) throws Throwable {

        //	上下文生命周期：

        if (ctx.isTerminated()) {
            return;
        }

        if (ctx.isWritten()) {
            ResponseHelper.writeResponse(ctx);
        }

        if (next != null) {
            next.doFilter(ctx, args);
        } else {
            //	没有下一个节点了，已经到了链表的最后一个节点
            ctx.terminated();
        }

    }


    @Override
    public void init() throws Exception {
        Filter.super.init();
    }


    @Override
    public void destroy() throws Exception {
        Filter.super.destroy();
    }


    /**
     * @一句话介绍： 创建一个过滤器实例
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 局部过滤器，是每个 ruleContext 一个实例
     * 全局过滤器，是 所有的 ruleContext 一个实例
     */
    @Override
    public Filter<GatewayContext> newInstance() throws Exception {
        return Filter.super.newInstance();
    }

    /**
     * @一句话介绍： 判断过滤器是不是全局的
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 默认是 全局的
     * 局部过滤器，是每个 ruleContext 一个实例
     * 全局过滤器，是 所有的 ruleContext 一个实例
     */
    @Override
    public boolean isGlobal() throws Exception {
        return global;
    }

    public void setNext(DefaultLinkedFilter<T> next) {
        this.next = next;
    }

    public DefaultLinkedFilter<T> getNext() {
        return next;
    }


    /**
     * @一句话介绍： 在链表的尾部添加元素
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @date
     * @详细介绍： 1.
     * 2.
     */
    public void addLast(DefaultLinkedFilter<T> filter) {


    }
}
