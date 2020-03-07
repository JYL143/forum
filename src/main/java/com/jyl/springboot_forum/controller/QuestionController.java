package com.jyl.springboot_forum.controller;


import com.jyl.springboot_forum.mapper.QuestionMapper;
import com.jyl.springboot_forum.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@Controller
public class QuestionController {


    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Integer id, Model model) {

        Question question=questionMapper.getById(id);
        model.addAttribute("question",question);
        return "question";
    }
}
