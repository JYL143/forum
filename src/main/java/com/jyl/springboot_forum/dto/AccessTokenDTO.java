package com.jyl.springboot_forum.dto;

import lombok.Data;


//一般要传递两个参数以上的我们一般封装成对象
@Data
public class AccessTokenDTO {
    private String client_id;           //github app里的，去查看对应的
    private String client_secret;       //github app里的，去查看对应的
    private String code;
    private String redirect_uri;        //授权成功后返回的地址
    private String state;
}
