package com.crazymaker.springcloud.base.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Payload<T> implements Serializable {
    private static final long serialVersionUID = -6981635654952282538L;

    //存储 uid
    private String id;
    //其他的用户信息
    private T userInfo;
    //过期时间
    private Date expiration;

    //校验的时候用
    private String token;


}
