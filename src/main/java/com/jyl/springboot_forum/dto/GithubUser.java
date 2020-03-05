package com.jyl.springboot_forum.dto;

import lombok.Data;

/**
 * 获取github  user的4个信息
 */
@Data
public class GithubUser {
    private String name;        //github用户名
    private Long id;            //github的啥id
    private String bio;         //github的bio
    private String avatarUrl;   //github头像url
}
