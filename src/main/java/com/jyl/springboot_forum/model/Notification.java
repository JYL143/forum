package com.jyl.springboot_forum.model;

import lombok.Data;

@Data
public class Notification {

    private Long id;            //主键
    private Long notifier;      //通知者,评论人id
    private Long receiver;      //发布者,被通知者
    private Long outerid;       //回复对象的id，比如评论id 问题id
    private Integer type;       //回复类型，0是表示问题评论通知，1表示二级评论通知，2表示点赞通知
    private Long gmtCreate;     //创建时间
    private Integer status;     //通知状态，0表示未读，1表示已读
    private String notifierName;//回复你的用户名字
    private String outerTitle;  //问题标题


}