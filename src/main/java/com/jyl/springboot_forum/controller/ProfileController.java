package com.jyl.springboot_forum.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyl.springboot_forum.mapper.QuestionMapper;

import com.jyl.springboot_forum.model.Question;
import com.jyl.springboot_forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class ProfileController {

//    @Autowired
//    private NotificationService notificationService;


    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "pn", defaultValue = "1") Integer pn) {

       User user = (User) request.getSession().getAttribute("user");
        if (user == null) {  //未登录用户返回主页面
            return "redirect:/";
        }

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
//            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
////            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".equals(action)) {
//            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
//            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }

        PageHelper.startPage(pn, 5);             //一页几条数据
        List<Question> questions=questionMapper.list2(user.getId());  //查询这个用户的所有问题
        PageInfo page=new PageInfo(questions,5); //分页条显示几个页
        model.addAttribute("pageinfo",page);
        return "profile";
    }
}
