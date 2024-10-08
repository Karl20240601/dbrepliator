package com.crazymaker.micro.core.servcie;


import com.crazymaker.micro.core.annotation.DefaultImplementor;
import com.crazymaker.micro.core.annotation.OverrideImplementor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Slf4j
public enum MicroCoreManager {
    INSTANCE;

    public static final String DEFAULT = "DEFAULT";
    private Map<Class, Map<String, MicroCore>> loadedServices = Collections.emptyMap();
    private Map<Class, MicroCore> bootedServices = new LinkedHashMap<Class, MicroCore>();


    // 加载和启动所有的微内核
    public void boot(Object config) {

        Map<String, String> coreConfig = Collections.emptyMap();
        //加载所有的服务
        loadedServices = loadAllServices();

        configBootedServices(coreConfig);

        //准备
        prepare(config);

        //启动
        startup();

        // 启动完成
        onComplete();
    }

    public void configBootedServices(Map<String, String> configs) {
        for (Class serviceClass : loadedServices.keySet()) {
            String clazzName = serviceClass.getName();

            String serviceType = configs.get(clazzName);
            if (StringUtils.isEmpty(serviceType)) {
                serviceType = DEFAULT;
            }
            Map<String, MicroCore> serviceMap = loadedServices.get(serviceClass);

            MicroCore microCore = serviceMap.get(serviceType);
            if (null == microCore) {
                microCore = serviceMap.get(DEFAULT);
            }
            bootedServices.put(serviceClass, microCore);

        }
    }

    public void shutdown() {
        for (MicroCore service : bootedServices.values()) {
            try {
                service.shutdown();
            } catch (Throwable e) {
                log.error(String.format("ServiceManager try to shutdown [{}] fail.", service.getClass().getName()), e);
            }
        }
    }

    //加载类路径下所有的微内核模块
    private Map<Class, Map<String, MicroCore>> loadAllServices() {
        Map<Class, Map<String, MicroCore>> tempLoadedServices = new LinkedHashMap<Class, Map<String, MicroCore>>();
        List<MicroCore> allServices = new LinkedList<MicroCore>();

        //加载所有服务，spi 服务
        load(allServices);

        //启动服务
        Iterator<MicroCore> serviceIterator = allServices.iterator();
        while (serviceIterator.hasNext()) {
            MicroCore microCore = serviceIterator.next();

            Class<? extends MicroCore> bootServiceClass = microCore.getClass();
            boolean isDefaultImplementor = bootServiceClass.isAnnotationPresent(DefaultImplementor.class);
            if (isDefaultImplementor) {
                if (!tempLoadedServices.containsKey(bootServiceClass)) {
                    //key = class对象， value = 微内核 对象

                    Map<String, MicroCore> innerMap = new LinkedHashMap<String, MicroCore>();
                    innerMap.put(DEFAULT, microCore);
                    tempLoadedServices.put(bootServiceClass, innerMap);
                } else {
                    //ignore the default service
                }
            } else {
                OverrideImplementor overrideImplementor = bootServiceClass.getAnnotation(OverrideImplementor.class);
                Class<? extends MicroCore> targetService = overrideImplementor.value();
                if (!tempLoadedServices.containsKey(targetService)) {

                    Map<String, MicroCore> innerMap = new LinkedHashMap<String, MicroCore>();
                    innerMap.put(microCore.getType(), microCore);
                    tempLoadedServices.put(targetService, innerMap);

                } else {

                    Map<String, MicroCore> innerMap = tempLoadedServices.get(targetService);
                    innerMap.put(microCore.getType(), microCore);

                }
            }

        }
        return tempLoadedServices;
    }

    private void prepare(Object config) {
        for (MicroCore service : bootedServices.values()) {
            try {
                service.prepare(config);
            } catch (Throwable e) {
                log.error(String.format("ServiceManager try to pre-start [{}] fail.", service.getClass().getName()), e);
            }
        }
    }

    private void startup() {
        for (MicroCore service : bootedServices.values()) {
            try {
                service.boot();
            } catch (Throwable e) {
                log.error(String.format("ServiceManager try to start [{}] fail.", service.getClass().getName()), e);
            }
        }
    }

    private void onComplete() {
        for (MicroCore service : bootedServices.values()) {
            try {
                service.onComplete();
            } catch (Throwable e) {
                log.error(String.format("Service [{}] AfterBoot process fails.", service.getClass().getName()), e);
            }
        }
    }

    /**
     * 查找服务
     * 时机： 用的时候
     * Find a {@link MicroCore} implementation, which is already started.
     *
     * @param serviceClass class name.
     * @param <T>          {@link MicroCore} implementation class.
     * @return {@link MicroCore} instance
     */
    public <T extends MicroCore> T findService(Class<T> serviceClass) {

        // 根据class 元数据，从已经 boot 启动了的微内核 列表里边去找
        T clazz = (T) bootedServices.get(serviceClass);
        if (null != clazz) return clazz;
        final MicroCore[] ret = {null};
        Iterator<Class> it = bootedServices.keySet().iterator();

        while (it.hasNext()) {
            Class next = it.next();
            //serviceClass 子类
            // next  接口
            if (serviceClass.isAssignableFrom(next))
                return (T) bootedServices.get(next);

        }

        return null;
    }

    // 加载服务
    // 时机： 启动的时候执行
    void load(List<MicroCore> allServices) {

        //通过java spi 查找服务
        Iterator<MicroCore> iterator = ServiceLoader.load(MicroCore.class).iterator();
        while (iterator.hasNext()) {
            allServices.add(iterator.next());
        }
    }
}
