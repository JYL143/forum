package com.jyl.springboot_forum.dto;

import lombok.Data;

/**
 * 获取github  user的3个信息
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
//    private String avatarUrl;
}
