package com.xj.community.provider;

import com.alibaba.fastjson.JSON;
import com.xj.community.dto.AccessTokenDTO;
import com.xj.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {

    // 返回token
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        // fasterJson 将对象转换为json
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            // access_token=2284e800c359b897c5cd5b2d4f21c55e7321c278&scope=&token_type=bearer
            String[] split = string.split("&");
            String  token = split[0].split("=")[1];
//            System.out.println("token:" + token);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 返回用户对象
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
