package com.jyl.springboot_forum.model;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String name;
    private String token;
    private String accountId;
    private Long gmtCreate;
    private Long gmtModified;




}