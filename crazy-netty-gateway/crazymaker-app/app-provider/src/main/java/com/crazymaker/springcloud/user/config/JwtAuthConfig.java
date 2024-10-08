package com.crazymaker.springcloud.user.config;

import com.crazymaker.springcloud.base.config.RsaKeyFileProperties;
import com.crazymaker.springcloud.base.service.JwtService;
import com.crazymaker.springcloud.base.service.impl.JwtServiceImpl;
import com.crazymaker.springcloud.standard.context.SpringContextUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(RsaKeyFileProperties.class)
public class JwtAuthConfig implements ApplicationContextAware {


    @Resource
    private RsaKeyFileProperties rsaKeyFileProperties;


    @Bean("jwtService")
    protected JwtService JwtServiceProvider() {

        return new JwtServiceImpl(rsaKeyFileProperties);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.setContext(applicationContext);

    }

}