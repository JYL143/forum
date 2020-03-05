package com.jyl.springboot_forum.model;

import lombok.Data;

@Data
public class User {

    private Integer id;         //主键
    private String name;        //github用户名
    private String token;       //用户的token，由uuid生成，作为持久化登录的东西
    private String accountId;   //github的accountId
    private Long gmtCreate;     //当前登录时间的毫秒数
    private Long gmtModified;   //当前登录时间的毫秒数
    private String bio;         //描述
    private String avatarUrl;   //github头像的url地址


}