package com.crazymaker.gateway.core.api.register;

import lombok.Data;

import java.io.Serializable;

/**
 * 注册中心的服务定义
 */
@Data
public class ServiceDefinition implements Serializable {

	private static final long serialVersionUID = -8263365765897285189L;

	/**
	 * 唯一的服务ID: serviceId:version
	 */
	private String uniqueId;

	/**
	 * 服务唯一id
	 */
	private String serviceId;

	/**
	 * 服务的版本号
	 */
	private String version;

	/**
	 * 服务的具体协议：http(mvc http) dubbo ..
	 */
	private String protocol;

	/**
	 * 路径匹配规则：访问真实ANT表达式：定义具体的服务路径的匹配规则
	 */
	private String patternPath;

	/**
	 * 环境名称
	 */
	private String envType;

	/**
	 * 服务启用禁用
	 */
	private boolean enable = true;


}