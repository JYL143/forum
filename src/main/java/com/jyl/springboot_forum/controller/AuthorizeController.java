package com.jyl.springboot_forum.controller;

import com.jyl.springboot_forum.dto.AccessTokenDTO;
import com.jyl.springboot_forum.dto.GithubUser;
import com.jyl.springboot_forum.mapper.NotificationMapper;
import com.jyl.springboot_forum.mapper.UserMapper;
import com.jyl.springboot_forum.model.User;
import com.jyl.springboot_forum.provider.GithubProvider;
import com.jyl.springboot_forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    //使用yml文件来配置这些信息，到时候就可以根据生产环境来改变这些值
    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;



    /**
     * 授权登录返回的方法
     * 首先在html页面一个超链接带参数进行访问github授权登录，然后授权成功后，里面参数指定授权成功来到这个方法，然后在返回下面两个参数
     * 拿到授权成功返回的code和state，然后在配上github app里的Client_id Client_secret Redirect_uri来获取access_token
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpSession session,
                           HttpServletResponse response){

        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);

        String accessToken=githubProvider.getAccessToken(accessTokenDTO);//用code和state来获取access_token
        GithubUser githubUser=githubProvider.getUser(accessToken);  //在用获取的access_token来获取user信息

        if (githubUser!=null && githubUser.getId() !=null){ //返回的user信息不为空就说明是登录成功

            User user=new User();
            String token=UUID.randomUUID().toString();
            user.setToken(token); //使用uuid当token,来当持久化状态id
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setBio(githubUser.getBio());
            user.setAvatarUrl(githubUser.getAvatarUrl()); //获取github的头像url
           userService.createOrUpdate(user); //判断要更新还是插入用户信息

            response.addCookie(new Cookie("token",token));

            return "redirect:/";//重定向到主页面方法 会改变url
        }else{ //登录失败
            return "redirect:/";
        }


    }

    //退出登录
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
