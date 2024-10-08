package com.crazymaker.gateway.core.api.filterchain;

/**
 * 执行过滤器的接口操作
 */
public interface Filter<T> {


    /**
     * @一句话介绍： 真正执行过滤器的方法
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1.
     * 2.
     */
    void doFilter(T t, Object... args) throws Throwable;

    /**
     * @一句话介绍： 触发下一个过滤器执行
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @date
     * @详细介绍： 1.
     * 2.
     */
    void fireNext(T t, Object... args) throws Throwable;


    /**
     * @一句话介绍： 过滤器初始化的方法
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1.
     * 2.
     */
    default void init() throws Exception {

    }

    /**
     * @一句话介绍： 过滤器销毁的方法
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1.
     * 2.
     */
    default void destroy() throws Exception {

    }


    /**
     * @一句话介绍： 创建一个过滤器实例
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 局部过滤器，是每个 ruleContext 一个实例
     * 全局过滤器，是 所有的 ruleContext 一个实例
     */
    default Filter<T> newInstance() throws Exception {
        return null;
    }


    /**
     * @一句话介绍： 判断过滤器是不是全局的
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 默认是 全局的
     * 局部过滤器，是每个 ruleContext 一个实例
     * 全局过滤器，是 所有的 ruleContext 一个实例
     */
    default boolean isGlobal() throws Exception {

        return true;
    }
}
