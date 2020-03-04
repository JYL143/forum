package com.jyl.springboot_forum.controller;

import com.jyl.springboot_forum.dto.AccessTokenDTO;
import com.jyl.springboot_forum.dto.GithubUser;
import com.jyl.springboot_forum.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


    /**
     * 授权登录返回的方法
     * 首先在html页面一个超链接带参数进行访问github授权登录，然后授权成功后，里面参数指定授权成功来到这个方法，然后在返回下面两个参数
     * 拿到授权成功返回的code和state，然后在配上github app里的Client_id Client_secret Redirect_uri来获取access_token
     * @param code
     * @param state
     * @return
     */
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code")String code,@RequestParam(name = "state")String state){

        AccessTokenDTO accessTokenDTO=new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);

        String accessToken=githubProvider.getAccessToken(accessTokenDTO);//用code和state来获取access_token
        GithubUser user=githubProvider.getUser(accessToken);  //在用获取的access_token来获取user信息
         System.out.println(user);

        return "index";
    }
}
