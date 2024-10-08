package com.crazymaker.gateway.register.nacos;

import com.alibaba.fastjson.JSON;
import com.crazymaker.gateway.core.api.config.Config;
import com.crazymaker.gateway.core.api.metadata.ServiceInstance;
import com.crazymaker.gateway.core.api.register.RegisterCenter;
import com.crazymaker.gateway.core.api.register.RegisterCenterListener;
import com.crazymaker.gateway.core.api.register.ServiceDefinition;
import com.crazymaker.micro.core.annotation.DefaultImplementor;
import com.crazymaker.springcloud.common.util.NetUtils;
import com.crazymaker.springcloud.common.util.ThreadUtil;
import com.crazymaker.springcloud.common.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingMaintainFactory;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.Service;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;

import static com.crazymaker.gateway.core.api.constant.BasicConst.COLON_SEPARATOR;
import static com.crazymaker.gateway.core.api.constant.BasicConst.META_DATA_KEY;
import static com.crazymaker.springcloud.common.util.BeanUtil.isNotEmpty;
import static com.crazymaker.springcloud.common.util.BeanUtil.newHashMap;

@DefaultImplementor
@Slf4j
public class NacosRegisterCenter implements RegisterCenter {
    private String registerAddress;
    private Config config ;
    private String groupName;


    //NamingService：实例管理的api ，
    // 其实现类是NacosNamingService，
    // 提供了注册实例、取消注册实例、获取指定服务实例，以及订阅指定服务以接收实例更改事件、取消订阅，关闭服务发现与注册等能力。
       private NamingService namingService;


    //NamingMaintainService： 服务管理的 api
    // 和Nacos相关的操作，直接操作Nacos服务器，提供了更新实例、查询服务、创建服务、删除服务、更新服务等能力。
    private NamingMaintainService namingMaintainService;

    //监听器列表
    private List<RegisterCenterListener> registerCenterListenerList = new CopyOnWriteArrayList<>();

    @Override
    public void init(String registerAddress, String groupName) {
        this.registerAddress = registerAddress;
        this.groupName = groupName;

        try {
            this.namingMaintainService = NamingMaintainFactory.createMaintainService(registerAddress);
            this.namingService = NamingFactory.createNamingService(registerAddress);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void register(ServiceDefinition serviceDefinition, ServiceInstance serviceInstance) {
        try {
            //构造nacos实例信息
            Instance nacosInstance = new Instance();
            nacosInstance.setInstanceId(serviceInstance.getServiceInstanceId());
            nacosInstance.setPort(serviceInstance.getPort());
            nacosInstance.setIp(serviceInstance.getIp());

            nacosInstance.setMetadata( newHashMap(META_DATA_KEY,
                    JSON.toJSONString(serviceInstance)));

            //注册
            namingService.registerInstance(serviceDefinition.getServiceId(), groupName, nacosInstance);

            //更新服务定义
            namingMaintainService.updateService(serviceDefinition.getServiceId(), groupName, 0,
                   newHashMap(META_DATA_KEY, JSON.toJSONString(serviceDefinition)));

            log.info("register {} {}", serviceDefinition, serviceInstance);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deregister(ServiceDefinition serviceDefinition, ServiceInstance serviceInstance) {
        try {
            namingService.registerInstance(serviceDefinition.getServiceId(),
                    groupName, serviceInstance.getIp(), serviceInstance.getPort());
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribeAllServices(RegisterCenterListener registerCenterListener) {
        registerCenterListenerList.add(registerCenterListener);
        doSubscribeAllServices();

        //可能有新服务加入，所以需要有一个定时任务来检查
        ScheduledExecutorService scheduledThreadPool = Executors
                .newScheduledThreadPool(1, new ThreadUtil.NameThreadFactory("nacos-subscribe-thread"));
        scheduledThreadPool.scheduleWithFixedDelay(() -> doSubscribeAllServices(),
                10, 600, TimeUnit.SECONDS);

    }

    private void doSubscribeAllServices() {
        try {
            //已经订阅的服务
            Set<String> subscribeService = namingService.getSubscribeServices().stream()
                    .map(ServiceInfo::getName).collect(Collectors.toSet());


            int pageNo = 1;
            int pageSize = 100;



            //分页从nacos拿到服务列表
            List<String> serviseList = namingService
                    .getServicesOfServer(pageNo, pageSize, groupName).getData();

            while (isNotEmpty(serviseList)) {
                log.info("service list size {}", serviseList.size());

                for (String service : serviseList) {
                    if (subscribeService.contains(service)) {
                        continue;
                    }

                    //nacos事件监听器
                    EventListener eventListener = new NacosRegisterListener();
                    eventListener.onEvent(new NamingEvent(service, null));
                    namingService.subscribe(service, groupName, eventListener);
                    log.info("subscribe {} {}", service, groupName);
                }

                serviseList = namingService
                        .getServicesOfServer(++pageNo, pageSize, groupName).getData();
            }

        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    public class NacosRegisterListener implements EventListener {

        @Override
        public void onEvent(Event event) {
            if (event instanceof NamingEvent) {
                NamingEvent namingEvent = (NamingEvent) event;
                String serviceName = namingEvent.getServiceName();

                try {
                    //获取服务定义信息
                    Service service = namingMaintainService.queryService(serviceName, groupName);
                    ServiceDefinition serviceDefinition = JSON.parseObject(service.getMetadata()
                            .get(META_DATA_KEY), ServiceDefinition.class);

                    //获取服务实例信息
                    List<Instance> allInstances = namingService.getAllInstances(service.getName(), groupName);
                    Set<ServiceInstance> set = new HashSet<>();

                    for (Instance instance : allInstances) {
                        ServiceInstance serviceInstance = JSON.parseObject(instance.getMetadata()
                                .get(META_DATA_KEY), ServiceInstance.class);
                        set.add(serviceInstance);
                    }

                    registerCenterListenerList.stream()
                            .forEach(l -> l.onChange(serviceDefinition, set));
                } catch (NacosException e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }




    private static ServiceInstance buildGatewayServiceInstance(Config config) {
        String localIp = NetUtils.getLocalIp();
        int port = config.getPort();
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setServiceInstanceId(localIp + COLON_SEPARATOR + port);
        serviceInstance.setIp(localIp);
        serviceInstance.setPort(port);
        serviceInstance.setRegisterTime(TimeUtil.currentTimeMillis());
        return serviceInstance;
    }

    private static ServiceDefinition buildGatewayServiceDefinition(Config config) {
        ServiceDefinition serviceDefinition = new ServiceDefinition();
        serviceDefinition.setUniqueId(config.getApplicationName());
        serviceDefinition.setServiceId(config.getApplicationName());
        serviceDefinition.setEnvType(config.getGroupName());
        return serviceDefinition;
    }

    // 来自于微内核
    @Override
    public String getType() {
        return null;
    }

    @Override
    public void prepare(Object params) throws Throwable {
        config= (Config) params;
    }

    @Override
    public void boot() throws Throwable {
        log.info("micro core is booted:" + NacosRegisterCenter.class);

        this.init(config.getRegistryAddress(), config.getGroupName());

        //构造网关服务定义和服务实例
        ServiceDefinition serviceDefinition = buildGatewayServiceDefinition(config);
        ServiceInstance serviceInstance = buildGatewayServiceInstance(config);

        //注册
        register(serviceDefinition, serviceInstance);

        //订阅
        subscribeAllServices(new RegisterCenterListener() {
            @Override
            public void onChange(ServiceDefinition serviceDefinition, Set<ServiceInstance> serviceInstanceSet) {
                log.info("refresh service and instance: {} {}", serviceDefinition.getUniqueId(),
                        JSON.toJSON(serviceInstanceSet));

            }
        });

    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {

    }



}
