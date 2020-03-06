package com.jyl.springboot_forum.model;

import lombok.Data;

@Data
public class Question {

    private Long id;
    private String title;           //标题
    private Long gmtCreate;         //发布时间毫秒数
    private Long gmtModified;       //修改时间毫秒数
    private Integer creator;        //关联user表的id值，多表查询用
    private Integer commentCount;   //回复数
    private Integer viewCount;      //浏览数
    private Integer likeCount;
    private String tag;             //问题标签
    private String description;     //问题补充

    private User user;              //多对一 获取头像用
}