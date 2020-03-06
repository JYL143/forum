package com.jyl.springboot_forum.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jyl.springboot_forum.mapper.QuestionMapper;
import com.jyl.springboot_forum.mapper.UserMapper;
import com.jyl.springboot_forum.model.Question;
import com.jyl.springboot_forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;
    /**
     * 主页面
     * cookie持久化登录
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(value = "pn",defaultValue = "1")Integer pn){

        //获取cookie，如果cokkie里有token，就获取该cookie的token，然后根据token查询表里的user信息
        Cookie[] cookies=request.getCookies();
        if (cookies !=null && cookies.length!=0){
        for (Cookie cookie:cookies){
            if (cookie.getName()!=null && cookie.getName().equals("token")){
                String token=cookie.getValue();
                User user=userMapper.findByToken(token);
                if (user!=null){ //这边如果查询也没问题，就说明是登录用户，这里在写session进去，利用cookie和session来做持久化操作
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
            }
        }

        PageHelper.startPage(pn, 5);             //一页几条数据
         List<Question> questions=questionMapper.list();
        PageInfo page=new PageInfo(questions,5); //分页条显示几个页

        model.addAttribute("pageinfo",page);

        return "index";
    }


}
