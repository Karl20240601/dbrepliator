package com.crazymaker.springcloud.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@Data
@Builder
@ApiModel("用户")
public class UserDTO implements Serializable {

    public UserDTO() {
    }

    public UserDTO(String appId, String account) {

        this.appId = appId;
        this.username = account;
    }

    //用户ID,前台用
    @ApiModelProperty("前台用户ID")
    private Long userId;
    //用户名
    @ApiModelProperty("用户名")
    private String username;
    //用户登录密码
    @ApiModelProperty("密码")
    private String password;
    //用户姓名
    @ApiModelProperty("昵称")
    private String nickname;
    //用户token
    @ApiModelProperty("令牌")
    private String token;


    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


    private String headImgUrl;
    private String mobile;
    private Integer sex;
    private Boolean enabled;
    private String type;
    private String appId;
    private String openId;
    private boolean isDel;

    public String buildSessionId() {
        return username + "-@-" + appId;
    }
}
