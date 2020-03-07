package com.jyl.springboot_forum.interceptor;

import com.jyl.springboot_forum.mapper.UserMapper;
import com.jyl.springboot_forum.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 做登录状态检查的拦截器,必须实现HandlerInterceptor接口
 */
@Component
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;  //这里使用这个注解需要加Component注解,mvc配置类也不能直接new这个类 也要注入

    //目标方法执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        Cookie[] cookies=request.getCookies();
        if (cookies !=null && cookies.length!=0){
            for (Cookie cookie:cookies){
                if (cookie.getName()!=null && cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    User user=userMapper.findByToken(token);
                    if (user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {

    }
}
