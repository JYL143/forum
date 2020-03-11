package com.jyl.springboot_forum.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyl.springboot_forum.mapper.NotificationMapper;
import com.jyl.springboot_forum.mapper.QuestionMapper;

import com.jyl.springboot_forum.model.Notification;
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

    @Autowired
    private NotificationMapper notificationMapper;


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

        Long unreadCount=notificationMapper.getnumber(user.getId());//查询该用户有几个未读的回复
        request.getSession().setAttribute("unreadCount",unreadCount);

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");

            PageHelper.startPage(pn, 5);
            List<Question> questions=questionMapper.list2(user.getId());  //查询这个用户的所有问题
            PageInfo page=new PageInfo(questions,5);
            model.addAttribute("pageinfo",page);

        } else if ("replies".equals(action)) {

            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");

            PageHelper.startPage(pn, 5);
            List<Notification> notifications=notificationMapper.list2(user.getId());  //查询这个用户的最新回复
            PageInfo page=new PageInfo(notifications,5);
            model.addAttribute("pageinfo",page);

        }


        return "profile";
    }
}
