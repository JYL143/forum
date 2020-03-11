package com.jyl.springboot_forum.mapper;


import com.jyl.springboot_forum.model.Comment;

import java.util.List;

public interface CommentMapper {

    //添加回复
    int insert(Comment comment);

    //查询一级回复
    List<Comment> listByTargetId1(Long id);

    //二级评论后 给一级评论添加回复
    void addCommentCount(Long parentId);

    //增加点赞数
    void addlike(Long parentId);

    //根据二级评论id来查询这个一级评论的发布人id
    Comment getUserIdByParentId(Long parentId);
}