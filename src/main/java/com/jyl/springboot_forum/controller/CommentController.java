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
        if (comment.getType()==1){ //一级评论在问题表增加回复数
            questionMapper.addCommentCount(comment.getParentId());//添加回复数  参数是父类id，一级就是指问题id，二级就是指回复id
        }else{ //2级评论在回复表增加回复数
            commentMapper.addCommentCount(comment.getParentId());//添加回复数  参数是父类id，一级就是指问题id，二级就是指回复id
        }
        return ResultDTO.okOf();
    }


    //增加点赞数
    @ResponseBody
    @PostMapping("/addlike")
    public Object addlike(Long parentId,HttpServletRequest request) {

        User user= (User) request.getSession().getAttribute("user");
        if (user ==null){
            return ResultDTO.errorOf(2002,"未登录，请先登录！");
        }

        commentMapper.addlike(parentId);//增加点赞数


        return ResultDTO.okOf();
    }



    //显示回复列表，暂时没用到，问题控制类直接调用了
    @ResponseBody
    @GetMapping("/comment/{id}")
    public ResultDTO<List<Comment>> comments(@PathVariable(name = "id") Long id) {
        List<Comment> commentList = commentMapper.listByTargetId1(id); //这里传的是问题id
        return ResultDTO.okOf(commentList);
    }


}
