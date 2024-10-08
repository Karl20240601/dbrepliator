package com.crazymaker.cloud.nacos.demo.gateway.starter;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.xiaoju.uemc.tinyid.client.utils.TinyId;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.autoconfigure.security.reactive.ReactiveManagementWebSecurityAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@EnableDiscoveryClient
@Slf4j


@SpringBootApplication(scanBasePackages = {
        "com.crazymaker.springcloud.user",
        "com.crazymaker.springcloud.base",
        "com.crazymaker.cloud.nacos.demo.gateway",
        "com.crazymaker.springcloud.standard",
        "com.crazymaker.springcloud.tinyid.config"
}, exclude = {
//        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class,
        ReactiveSecurityAutoConfiguration.class,
        ReactiveManagementWebSecurityAutoConfiguration.class,
        //排除db的自动配置
        ReactiveUserDetailsServiceAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        //排除redis的自动配置
        RedisAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class,
        //排除 druid,
        DruidDataSourceAutoConfigure.class,
        ManagementWebSecurityAutoConfiguration.class,
        //排除 spring  Security
        ManagementWebSecurityAutoConfiguration.class,
})

public class GatewayProviderApplication {
    public static void main(String[] args) {

        //在服务的启动命令中加上 -Dcsp.sentinel.app.type=1
        // 或者
//        System.setProperty("csp.sentinel.app.type", "1");
        ConfigurableApplicationContext applicationContext = null;
        try {
            applicationContext = SpringApplication.run(GatewayProviderApplication.class, args);
            System.out.println("Server startup done.");
        } catch (Exception e) {
            log.error("服务启动报错", e);
            return;
        }

        Environment env = applicationContext.getEnvironment();
        String port = env.getProperty("server.port");
        String name = env.getProperty("spring.application.name");

        String path = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(path)) {
            path = "";
        }
        String ip = env.getProperty("spring.cloud.client.ip-address");

        System.out.println("\n----------------------------------------------------------\n\t" +
                name.toUpperCase() + " is running! Access URLs:\n\t" +
                "Local: \t\thttp://" + ip + ":" + port + path + "/\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + port + path + "/doc.html\n\t" +
                "actuator: \thttp://" + ip + ":" + port + path + "/actuator/info\n\t" +
                "----------------------------------------------------------");
//        Long id = TinyId.nextId("test");
//        System.out.println("current id is: " + id);

    }


    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}") String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }
}