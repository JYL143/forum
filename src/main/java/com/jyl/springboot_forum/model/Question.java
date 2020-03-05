package com.jyl.springboot_forum.model;

import lombok.Data;

@Data
public class Question {

    private Long id;
    private String title;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private String description;

}