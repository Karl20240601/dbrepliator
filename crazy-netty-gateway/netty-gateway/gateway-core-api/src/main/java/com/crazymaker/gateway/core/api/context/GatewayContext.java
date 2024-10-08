package com.crazymaker.gateway.core.api.context;

import com.crazymaker.gateway.core.api.metadata.Rule;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import io.netty.util.DefaultAttributeMap;
import io.netty.util.ReferenceCountUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Builder
public class GatewayContext {


    /**
     * 一个请求正在执行中的状态
     */
    public static final int RUNNING = 0;
    /**
     * 标志请求结束，写回Response
     */
    public static final int WRITTEN = 1;
    /**
     * 写回成功后，设置该标识，如果是Netty ，ctx.WriteAndFlush(response)
     */
    public static final int COMPLETED = 2;
    /**
     * 整个网关请求完毕，彻底结束
     */
    public static final int TERMINATED = -1;


    private GatewayRequest request;

    private GatewayResponse response;

    private Rule rule;

    @Setter
    @Getter
    private boolean gray;

    /**
     * @一句话介绍： 具体的路由实例
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 1 先通过  注册中心找到所有的实例
     * 2 后通过 路由策略进行路由
     */
    private ServiceInstance chosen;

    /**
     * 转发协议
     */
    protected final String protocol;

    /**
     * 上下文状态
     */
    protected volatile int status = RUNNING;
    /**
     * Netty上下文
     */
    protected final ChannelHandlerContext nettyCtx;

    /**
     * 上下文参数集合
     * <p>
     * /**
     *
     * @一句话介绍： map 操作数组化
     * @author 40岁老架构师 尼恩 @ 公众号 技术自由圈
     * @详细介绍： 大型系统里多个模块间经常通过 Map 来交互信息，互相只需要耦合 String 类型
     * 的 key。常见代码如下：
     * public static final String key = "mykey";
     * Map<String,Object> attributeMap = new HashMap<String,Object>();
     * Object value = attributeMap.get(key);
     * <p>
     * 大量的 Map 操作也是性能的一大消耗点。
     * <p>
     * 如何将 将 Map 操作进行了优化，改进为数组操作，避免了 Map 操作消耗。
     * <p>
     * 新的范例代码如下：
     * public static final AttributeNamespace ns = AttributeNamespace.createNamespace("mynamespace");
     * public static final AttributeKey key = new AttributeKey(ns, "mykey");
     * DefaultAttributeMap attributeMap = new DefaultAttributeMap(ns, 8);
     * Object value = attributeMap.get(key);
     * 工作机制简单说明如下：
     * 1. key 类型由 String 改为自定义的 AttributeKey，AttributeKey 会在初始化阶
     * 段就去 AttributeNamespace 申请一个固定 id
     * 2.map 类 型 由 HashMap 改 为 自 定 义 的 DefaultAttributeMap，DefaultAttributeMap 内部使用数组存放数据
     * 3. 操作 DefaultAttributeMap 直接使用 AttributeKey 里存放的 id 作为 index 访问数组即可，避免了 hash 计算等一系列操作。
     * 核心就是将之前的字符串 key和一个固定的 id 对应起来，作为访问数组的 index对比 HashMap 和 DefaultAttributeMap，
     * 性能提升约 30%。
     */
    protected final AttributeMap attributes = new DefaultAttributeMap();

    /**
     * 请求过程中发生的异常
     */
    protected Throwable throwable;
    /**
     * 是否保持长连接
     */
    protected final boolean keepAlive;

    /**
     * 是否已经释放资源
     */
    protected final AtomicBoolean requestReleased = new AtomicBoolean(false);


    /**
     * SR(Server[Rapid-Core] Received):	服务器接收到网络请求
     * SS(Server[Rapid-Core] Send):		服务器写回请求
     * RS(Route Send):						客户端发送请求
     * RR(Route Received): 				客户端收到请求
     */

    protected long SRTime;

    protected long SSTime;

    protected long RSTime;

    protected long RRTime;


    public GatewayRequest getRequest() {
        return request;
    }

    public void setRequest(GatewayRequest request) {
        this.request = request;
    }


    public GatewayResponse getResponse() {
        return response;
    }

    public void setResponse(GatewayResponse response) {
        this.response = response;
    }


    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }


    /**
     * 重写父类释放资源方法，用于正在释放资源
     */
    public void releaseRequest() {
        if (requestReleased.compareAndSet(false, true)) {
            ReferenceCountUtil.release(request.getFullHttpRequest());
        }
    }

    /**
     * 获取原始的请求对象
     *
     * @return
     */
    public GatewayRequest getOriginRequest() {
        return request;
    }


    public void running() {
        status = RUNNING;
    }


    public void written() {
        status = WRITTEN;
    }


    public void completed() {
        status = COMPLETED;
    }


    public void terminated() {
        status = TERMINATED;
    }


    public boolean isRunning() {
        return status == RUNNING;
    }


    public boolean isWritten() {
        return status == WRITTEN;
    }


    public boolean isCompleted() {
        return status == COMPLETED;
    }


    public boolean isTerminated() {
        return status == TERMINATED;
    }

    public void setThrowable(Throwable e) {
        throwable = e;
    }

    public boolean isError() {
        return throwable != null;
    }


    public <T> T attr(AttributeKey<T> key) {
        return attributes.attr(key).get();
    }

    public <T> void setAttr(AttributeKey<T> key, T val) {
        attributes.attr(key).set(val);
    }
}
