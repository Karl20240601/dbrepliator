package com.redis;

import com.redis.test.Test1;
import com.redis.test.TestConfiguration;
import com.redis.test.TestService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();
        annotationConfigApplicationContext.setEnvironment(buildEnvirment());
        annotationConfigApplicationContext.register(Test1.class);
        annotationConfigApplicationContext.register(TestConfiguration.class);
        annotationConfigApplicationContext.register(TestService.class);
        annotationConfigApplicationContext.register(RedisContext.class);
        annotationConfigApplicationContext.refresh();

        TestService bean = annotationConfigApplicationContext.getBean(TestService.class);
        bean.write("test_1234","1111111111111111111111111");
        bean.write("test_4567","2222222222222222222222222");
        System.out.println( bean.read("test_1234"));
        System.out.println( bean.read("test_4567"));

    }

    private static ConfigurableEnvironment buildEnvirment(){
        StandardEnvironment standardEnvironment = new StandardEnvironment();
        Map<String,Object> map = new HashMap<>();
        map.put("redis.sharding.shpere.enable","true");
        map.put("redis.tagkey.mapping","{\"test_1234\":\"tag1\",\"test_4567\":\"tag2\"}");
        map.put("sharding.shphere.redis.hostConfigs[0].hosts","47.121.138.167:6379");
        map.put("sharding.shphere.redis.hostConfigs[0].tag","tag1");
        map.put("sharding.shphere.redis.hostConfigs[0].role","peer");
        map.put("sharding.shphere.redis.hostConfigs[0].clusterType","standaloan");

        map.put("sharding.shphere.redis.hostConfigs[1].hosts","47.121.138.167:6380");
        map.put("sharding.shphere.redis.hostConfigs[1].tag","tag2");
        map.put("sharding.shphere.redis.hostConfigs[1].role","peer");
        map.put("sharding.shphere.redis.hostConfigs[1].clusterType","standaloan");

        MapPropertySource mapPropertySource = new MapPropertySource("messageConfig",map);
        standardEnvironment.getPropertySources().addLast(mapPropertySource);
        return standardEnvironment;
    }
}
