package com.jyl.springboot_forum.controller;


import com.jyl.springboot_forum.dto.ResultDTO;
import com.jyl.springboot_forum.mapper.CommentMapper;
import com.jyl.springboot_forum.mapper.NotificationMapper;
import com.jyl.springboot_forum.mapper.QuestionMapper;
import com.jyl.springboot_forum.model.Comment;
import com.jyl.springboot_forum.model.Notification;
import com.jyl.springboot_forum.model.Question;
import com.jyl.springboot_forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 评论控制类
 * 添加通知
 */
@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private NotificationMapper notificationMapper;

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


        Notification notification=new Notification();
        notification.setNotifier(comment.getCommentator()); //评论人id
        notification.setOuterid(comment.getParentId());     //问题或者评论id(一级就是指问题id，二级就是指回复id)
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setStatus(0);
        notification.setNotifierName(user.getName());//回复你的用户名字

        if (comment.getType()==1){ //一级评论在问题表增加回复数

            questionMapper.addCommentCount(comment.getParentId());//添加回复数  参数是父类id，一级就是指问题id，二级就是指回复id

            //先查询发布人id，然后添加通知
            Question question=questionMapper.getUserIdByParentId(comment.getParentId());//根据问题id来查询这个问题的发布人id和问题标题
            notification.setReceiver(Long.valueOf(question.getCreator())); //发布人id
            notification.setType(0);     //0是表示问题评论通知，1表示二级评论通知，2表示点赞通知
            notification.setOuterTitle(question.getTitle());
            notificationMapper.insert(notification);//添加通知
        }else{ //2级评论在回复表增加回复数

            commentMapper.addCommentCount(comment.getParentId());//添加回复数  参数是父类id，一级就是指问题id，二级就是指回复id

            //先查询发布人id，然后添加通知
            Comment comment1=commentMapper.getUserIdByParentId(comment.getParentId());//根据二级评论id来查询这个一级评论的发布人id和一级评论内容
            notification.setReceiver(comment1.getCommentator()); //发布人id
            notification.setOuterTitle(comment1.getContent());
            notification.setType(1);     //0是表示问题评论通知，1表示二级评论通知，2表示点赞通知
            notificationMapper.insert(notification);//添加通知
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



    //显示二级评论
    @ResponseBody
    @GetMapping("/comment/{id}")
    public ResultDTO<List<Comment>> comments(@PathVariable(name = "id") Long id) {
        List<Comment> commentList = commentMapper.listByTargetId1(id); //这里传的是问题id
        return ResultDTO.okOf(commentList);
    }


}
