package com.jyl.springboot_forum.controller;


import com.jyl.springboot_forum.mapper.CommentMapper;
import com.jyl.springboot_forum.mapper.QuestionMapper;
import com.jyl.springboot_forum.model.Comment;
import com.jyl.springboot_forum.model.Question;
import com.jyl.springboot_forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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


    //查看问题详情  加浏览数
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model) {

        questionMapper.addviewCount(id); //增加阅读数
        Question question=questionMapper.getById(id); //查询问题
        List<Question> relatedQuestions=questionService.selectRelated(question); //按标签数组查询相关问题，先将标签字符串的，改成|,以供数据库正则查询
        List<Comment> comments = commentMapper.listByTargetId1(Long.valueOf(id));//查询一级回复

        model.addAttribute("question",question);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);

        return "question";
    }
}
