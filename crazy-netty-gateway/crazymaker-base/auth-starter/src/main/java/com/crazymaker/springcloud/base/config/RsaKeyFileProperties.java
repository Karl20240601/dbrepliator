package com.crazymaker.springcloud.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rsa.key")
public class RsaKeyFileProperties {
    private String publicKeyFile;
    private String privateKeyFile;

}
