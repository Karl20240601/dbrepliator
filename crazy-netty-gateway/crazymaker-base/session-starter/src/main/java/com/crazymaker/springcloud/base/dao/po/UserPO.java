package com.crazymaker.springcloud.base.dao.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "USER_INFO")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPO implements Serializable {

    //用户ID
    @Id
    @GenericGenerator(
            name = "snowflakeIdGenerator",
            strategy = "com.crazymaker.springcloud.standard.hibernate.CommonZkSequenceIdGenerator")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "snowflakeIdGenerator")
    @Column(name = "USER_ID", unique = true, nullable = false, length = 18)
    private Long userId;

    //用户姓名
    @Column(name = "NICK_NAME", length = 255)
    private String nickname;

    //用户名
    @Column(name = "USER_NAME", length = 255)
    private String username;
    //用户登录密码
    @Column(name = "PASSWORD", length = 255)
    private String password;


    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "CREATE_TIME")
    private Date createTime;

    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "UPDATE_TIME")
    private Date updateTime;


    @Column(name = "HEAD_IMG_URL")
    private String headImgUrl;
    @Column(name = "MOBILE")
    private String mobile;
    @Column(name = "SEX")
    private Integer sex;
    @Column(name = "ENABLED")
    private Boolean enabled;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "OPEN_ID")
    private String openId;
    @Column(name = "IS_DEL")
    private Boolean isDel;

}
