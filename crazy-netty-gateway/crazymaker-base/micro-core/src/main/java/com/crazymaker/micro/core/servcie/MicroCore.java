package com.crazymaker.micro.core.servcie;


//生命周期
public interface MicroCore {

    /**
     * 获取 微内核的 类型名称，
     * 和配置的名称相对应
     *
     * @return
     */
    String getType();

    void prepare(Object params) throws Throwable;


    void boot() throws Throwable;

    void onComplete() throws Throwable;

    void shutdown() throws Throwable;
}
