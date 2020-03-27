package com.xj.community.controller;

import com.xj.community.mapper.UserMapper;
import com.xj.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.Session;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies !=null && cookies.length != 0){
            for(Cookie cookie: cookies){
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    System.out.println(">>>>>>>>>>>"+token);
                    User user = userMapper.findByToken(token);
                    if (user != null){
                        // getSession做了两件事，返回浏览器生成cookie，生成user对象的hash值
                        System.out.println(request.getSession().toString());
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }

        return "index";
    }

}
