package com.jyl.springboot_forum.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.jyl.springboot_forum.mapper.QuestionMapper;

import com.jyl.springboot_forum.model.Question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionMapper questionMapper;



    /**
     * 主页面
     * cookie持久化登录
     * 标签分类查询
     * @return
     */
    @GetMapping("/")
    public String index( Model model,
                         @RequestParam(value = "pn",defaultValue = "1")Integer pn,
                         @RequestParam(name = "tag", required = false) String tag){


        if (tag==null){
            PageHelper.startPage(pn, 5);             //一页几条数据
            List<Question> questions=questionMapper.list();
            PageInfo page=new PageInfo(questions,5); //分页条显示几个页
            model.addAttribute("pageinfo",page);
            return "index";
        }else {  //查询指定标签问题
            PageHelper.startPage(pn, 5);
            List<Question> questions=questionMapper.listbytag(tag);
            PageInfo page=new PageInfo(questions,5);
            model.addAttribute("pageinfo",page);
            model.addAttribute("tag1",tag);
            return "tag";
        }

    }

    /**
     * 搜索功能
     * @return
     */
    @GetMapping("/search")
    public String search( Model model,
                          @RequestParam(value = "pn",defaultValue = "1")Integer pn,
                          @RequestParam(name = "search", required = false) String search){
        model.addAttribute("search",search);
        PageHelper.startPage(pn, 5);
        List<Question> questions=questionMapper.searchTitle(search);
        PageInfo page=new PageInfo(questions,5);
        model.addAttribute("pageinfo",page);
        return "search";
    }
}
