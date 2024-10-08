package com.crazymaker.springcloud.standard.context;

import com.crazymaker.springcloud.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * Created by 尼恩 on 2019/7/18.
 */
@Slf4j
public class SpringContextUtil {
    /**
     * 上下文对象实例
     */
    private static org.springframework.context.ApplicationContext applicationContext;


    public static void setContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * 获取applicationContext
     *
     * @return
     */
    public static org.springframework.context.ApplicationContext getApplicationContext() {

        if (null != applicationContext) {
            return applicationContext;
        } else {

            String msg = "applicationContext 还为空，异常";
            throw BusinessException.builder().errMsg(msg).build();
        }

    }

    /**
     * 通过name获取 Bean.
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, ApplicationContext context) {
        if (null == applicationContext) {
            applicationContext = context;
        }
        return getApplicationContext().getBean(clazz);
    }


    /**
     * 获取getBeanDefinitionNames.
     *
     * @return getBeanDefinitionNames
     */
    public static List<String> getBeanDefinitionNames() {
        String[] names = getApplicationContext().getBeanDefinitionNames();
        return Arrays.asList(names);

    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}