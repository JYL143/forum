package com.jyl.springboot_forum.controller;


import com.jyl.springboot_forum.mapper.CommentMapper;
import com.jyl.springboot_forum.mapper.NotificationMapper;
import com.jyl.springboot_forum.mapper.QuestionMapper;
import com.jyl.springboot_forum.model.Comment;
import com.jyl.springboot_forum.model.Notification;
import com.jyl.springboot_forum.model.Question;
import com.jyl.springboot_forum.model.User;
import com.jyl.springboot_forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 问题控制台
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionMapper questionMapper;


    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationMapper notificationMapper;


    //查看问题详情  加浏览数,更新回复数，更改通知状态
    @Transactional //开启事务
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request,
                           @RequestParam(name = "notificationid", required = false) String notificationid) {

        questionMapper.addviewCount(id); //增加阅读数
        Question question=questionMapper.getById(id); //查询问题
        List<Question> relatedQuestions=questionService.selectRelated(question); //按标签数组查询相关问题，先将标签字符串的，改成|,以供数据库正则查询
        List<Comment> comments = commentMapper.listByTargetId1(Long.valueOf(id));//查询一级回复


        model.addAttribute("question",question);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);

        if (notificationid!=null){

            notificationMapper.updateStatus(Integer.valueOf(notificationid)); //将未读改成已读
            //查询该用户有几个未读的回复
            User user = (User) request.getSession().getAttribute("user");
            Long unreadCount=notificationMapper.getnumber(user.getId());
            request.getSession().setAttribute("unreadCount",unreadCount);
        }
        return "question";
    }

    //查看问题详情  去掉未读消息，加浏览数,这个先查二级评论对应的一级评论 在对应的问题id
    @Transactional //开启事务
    @GetMapping("/notification/{id}")
    public String notification(@PathVariable(name = "id") Integer id, Model model, HttpServletRequest request) {

        Long outerid=notificationMapper.getouterid(id); //查询主键对应的outerid

        Comment comment=commentMapper.getUserIdByParentId(outerid);  //查询评论对应的问题id
        Integer questionid=comment.getParentId().intValue();                 //long转int类型

        notificationMapper.updateStatus(id); //将未读改成已读

        questionMapper.addviewCount(questionid); //增加阅读数
        Question question=questionMapper.getById(questionid); //查询问题
        List<Question> relatedQuestions=questionService.selectRelated(question); //按标签数组查询相关问题，先将标签字符串的，改成|,以供数据库正则查询
        List<Comment> comments = commentMapper.listByTargetId1(comment.getParentId());//查询一级回复

        //查询该用户有几个未读的回复
        User user = (User) request.getSession().getAttribute("user");
        Long unreadCount=notificationMapper.getnumber(user.getId());
        request.getSession().setAttribute("unreadCount",unreadCount);

        model.addAttribute("question",question);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);

        return "question";
    }
}
