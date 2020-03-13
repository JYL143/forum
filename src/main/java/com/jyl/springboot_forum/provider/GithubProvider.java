package com.jyl.springboot_forum.provider;

import com.alibaba.fastjson.JSON;
import com.jyl.springboot_forum.dto.AccessTokenDTO;
import com.jyl.springboot_forum.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

/**
 * 使用okhttp获取github的AccessToken,已经user
 */

@Component
public class GithubProvider {

    /**
     * 获取github的AccessToken
     * @param accessTokenDTO
     * @return
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];  //拆分获取到的access_token  access_token=c3f5001642cc52377850bb2244c7b23b9a9acf93&scope=&token_type=bearer

            return token;
        } catch (Exception e) {
//            log.error("getAccessToken error,{}", accessTokenDTO, e);
             System.out.println("getAccessToken方法失败");
              
        }
        return null;
    }

    //使用获取的access_token来获取用户的信息
    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();

            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);  //将string类型的json对象转成该对象类型
            return githubUser;
        } catch (Exception e) {
           // log.error("getUser error,{}", accessToken, e);
            System.out.println("登录失败，github密码登录延迟，晚点登");
        }
        return null;
    }

}
