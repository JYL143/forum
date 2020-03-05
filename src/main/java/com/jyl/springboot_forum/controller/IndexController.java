package com.jyl.springboot_forum.controller;

import com.jyl.springboot_forum.mapper.UserMapper;
import com.jyl.springboot_forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {



    @Autowired
    private UserMapper userMapper;

    /**
     * 主页面
     * cookie持久化登录
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request){

        //获取cookie，如果cokkie里有token，就获取该cookie的token，然后根据token查询表里的user信息
        Cookie[] cookies=request.getCookies();
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

        return "index";
    }

}
