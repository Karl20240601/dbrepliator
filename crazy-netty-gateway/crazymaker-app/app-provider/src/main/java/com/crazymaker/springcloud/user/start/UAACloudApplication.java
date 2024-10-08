package com.crazymaker.springcloud.user.start;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.crazymaker.springcloud.standard.config.TokenFeignConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableAutoConfiguration(exclude = {
        //排除db的自动配置
//        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class,
        //排除redis的自动配置
//        RedisAutoConfiguration.class,
//        RedisRepositoriesAutoConfiguration.class,

        //排除 druid
        DruidDataSourceAutoConfigure.class,
        ManagementWebSecurityAutoConfiguration.class,
        //排除 spring  Security
        SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class,
})
@SpringBootApplication(scanBasePackages = {
        "com.crazymaker.springcloud.message",
        "com.crazymaker.springcloud.user",
        "com.crazymaker.springcloud.base",
        "com.crazymaker.springcloud.seckill.remote.fallback",
        "com.crazymaker.springcloud.standard"
})
//@EnableScheduling
@EnableSwagger2
@EnableJpaRepositories(basePackages = {
        "com.crazymaker.springcloud.message.dao",
        "com.crazymaker.springcloud.user.dao",
        "com.crazymaker.springcloud.base.dao"
})

//@EnableRedisRepositories(basePackages = {
//        "com.crazymaker.springcloud.user.*.redis"})

@EntityScan(basePackages = {
        "com.crazymaker.springcloud.message.dao.po",
        "com.crazymaker.springcloud.user.dao.po",
        "com.crazymaker.springcloud.base.dao.po",
        "com.crazymaker.springcloud.standard.*.dao.po"
})
/**
 *  启用 Eureka Client 客户端组件
 */
@EnableDiscoveryClient
//启动Feign
@EnableFeignClients(basePackages =
        {"com.crazymaker.springcloud.seckill.remote.client"}
        , defaultConfiguration = {TokenFeignConfiguration.class}
)
@Slf4j
//@EnableHystrix
public class UAACloudApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(UAACloudApplication.class, args);

        Environment env = applicationContext.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
//        String ip = env.getProperty("eureka.instance.ip-address");
        String ip = "127.0.0.1";

        log.info("\n----------------------------------------------------------\n\t" +
                "UAA 用户账号与认证服务 is running! Access URLs:\n\t" +
                "Local: \t\thttp://" + ip + ":" + port + path + "/\n\t" +
                "swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
                "actuator: \thttp://" + ip + ":" + port + path + "/actuator/info\n\t" +
                "----------------------------------------------------------");


    }


}