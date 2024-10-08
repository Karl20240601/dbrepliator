package com.crazymaker.gateway.core.api.metadata;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.crazymaker.gateway.core.api.constant.BasicConst.PREFIX_PATH_MATCHER;

/**
 * 核心规则类
 */
@Slf4j
@Data
public class Rule implements Serializable {

    /**
     * 规则ID，全局唯一
     */
    private String id;

    /**
     * 规则名称
     */
    private String name;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 后端服务ID
     */
    private String serviceId;
    /**
     * 请求前缀
     */
    private String prefix;


    /**
     * 匹配类型： 前缀匹配 ，模式匹配 （带占位符）
     * <p>
     * //前缀匹配
     * String  PREFIX_PATH_MATCHER="prefix";
     * //ant风格的路径匹配就是通过独特的匹配符简化路径匹配。
     * String  ANT_PATH_MATCHER="ant_path";
     */
    private String matchType = PREFIX_PATH_MATCHER;
    /**
     * upstream 路径
     */
    private String upstreamPath;


    private Set<FilterParam> filterParams = new HashSet<>();
    private String balanceStrategy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public Set<FilterParam> getFilterParams() {
        return filterParams;
    }

    public void setFilterParams(Set<FilterParam> filterParams) {
        this.filterParams = filterParams;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUpstreamPath() {
        return upstreamPath;
    }

    public void setUpstreamPath(String upstreamPath) {
        this.upstreamPath = upstreamPath;
    }

    public Rule() {
        super();
    }

    public Rule(String id, String name, String protocol, String serviceId, String prefix, String upstreamPath, Integer order, Set<FilterParam> filterParams) {
        this.id = id;
        this.name = name;
        this.protocol = protocol;
        this.serviceId = serviceId;
        this.prefix = prefix;
        this.upstreamPath = upstreamPath;
        this.filterParams = filterParams;
    }

    public boolean isHttpService() {
        return protocol.toLowerCase().startsWith("http");
    }

    public ServiceInstance simpleServiceInstance() {
        //  从http url 提取 端口
        ServiceInstance serviceInstance = new ServiceInstance();

        try {
            URI uri = new URI(this.upstreamPath);
            serviceInstance.setScheme(uri.getScheme());
            serviceInstance.setIp(uri.getHost());
            serviceInstance.setPort(uri.getPort());
            serviceInstance.setPath(uri.getPath());
            return serviceInstance;
        } catch (URISyntaxException e) {
            log.error("路径有误" + this.upstreamPath, e);
        }
        return serviceInstance;
    }


    public static class FilterParam {

        /**
         * 过滤器唯一ID
         */
        private String key;
        /**
         * 过滤器规则描述，{"timeOut":500,"balance":random}
         */
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if ((o == null) || getClass() != o.getClass()) {
                return false;
            }

            FilterParam that = (FilterParam) o;
            return key.equals(that.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }

    /**
     * 向规则里面添加过滤器
     *
     * @param filterParam
     * @return
     */
    public boolean addFilterConfig(FilterParam filterParam) {
        return filterParams.add(filterParam);
    }

    /**
     * 通过一个指定的FilterID获取FilterConfig
     *
     * @param id
     * @return
     */
    public FilterParam getFilterParam(String id) {
        for (FilterParam config : filterParams) {
            if (config.getKey().equalsIgnoreCase(id)) {
                return config;
            }

        }
        return null;
    }

    /**
     * 根据filterID判断当前Rule是否存在
     *
     * @return
     */
    public boolean hashId(String id) {
        for (FilterParam filterParam : filterParams) {
            if (filterParam.getKey().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if ((o == null) || getClass() != o.getClass()) {
            return false;
        }

        FilterParam that = (FilterParam) o;
        return id.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
