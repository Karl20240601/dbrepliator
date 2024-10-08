package com.crazymaker.gateway.bootstrap;

import com.crazymaker.gateway.core.api.config.Config;
import com.crazymaker.springcloud.base.auth.AuthUtils;
import com.crazymaker.springcloud.base.config.RsaKeyFileProperties;
import com.crazymaker.springcloud.base.service.impl.JwtServiceImpl;

/**
 * @ClassName JwtServceBootstrap
 * @Author 40岁老架构师 尼恩 @ 公众号 技术自由圈
 */

public class JwtServiceBootstrap {


    public  static void start(Config config)
    {
        RsaKeyFileProperties  properties=new RsaKeyFileProperties();
//        properties.setPrivateKeyFile(config.getPrivateKeyFile());
        properties.setPublicKeyFile(config.getPublicKeyFile());
        AuthUtils.setRsaJwtService(new JwtServiceImpl(properties));
    }

}
