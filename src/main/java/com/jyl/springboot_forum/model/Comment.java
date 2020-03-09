package com.jyl.springboot_forum.model;

import lombok.Data;

@Data
public class Comment {

    private Long id;
    private Long parentId;          //问题ID
    private Integer type;           //父类类型，1级评论还是二级评论
    private Long commentator;       //评论人id
    private Long gmtCreate;         //创建时间
    private Long gmtModified;       //更新时间
    private Long likeCount;         //点赞数
    private String content;         //评论内容
    private Integer commentCount;   //该回复的回复数
    private User user;
}