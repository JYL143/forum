package com.jyl.springboot_forum.controller;


import com.jyl.springboot_forum.dto.ResultDTO;
import com.jyl.springboot_forum.enums.CommentTypeEnum;
import com.jyl.springboot_forum.mapper.CommentMapper;
import com.jyl.springboot_forum.mapper.QuestionMapper;
import com.jyl.springboot_forum.model.Comment;
import com.jyl.springboot_forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 评论控制类
 */
@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Transactional //开启事务
    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody Comment comment,
                       HttpServletRequest request) {//将json对象自动封装到该参数对象里

        User user= (User) request.getSession().getAttribute("user");
        if (user ==null){
            return ResultDTO.errorOf(2002,"未登录不能进行评论，请先登录！");
        }
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(Long.valueOf(user.getId()));
        comment.setLikeCount(0L); //点赞数
        comment.setCommentCount(0);   //该回复的回复数
        commentMapper.insert(comment);  //添加回复
        questionMapper.addCommentCount(comment.getParentId());//添加回复数

        return ResultDTO.okOf();
    }

    //显示回复列表
    @ResponseBody
    @GetMapping("/comment/{id}")
    public ResultDTO<List<Comment>> comments(@PathVariable(name = "id") Long id) {
        List<Comment> commentList = commentMapper.listByTargetId1(id); //这里传的是问题id
        return ResultDTO.okOf(commentList);
    }


}
