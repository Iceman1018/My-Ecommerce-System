package com.example.trade.user.API;


import com.example.trade.user.Service.UserService;
import com.example.trade.user.db.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;


@Slf4j
@Controller
public class UserAPI {
    @Autowired
    private UserService userService;


    @GetMapping("/api/user/create")
    @ResponseBody
    public User createUser(String userName, String email, String password, String tag){
        log.info("createUser user:{}",userName);
        return userService.createUser(userName,email,password,tag);
    }

    @GetMapping("/api/user/login")
    @ResponseBody
    public User UserLogin(@RequestParam("email") String email, @RequestParam("password") String password){
        log.info("userLogin email:{}",email);
        return userService.UserLogin(email,password);
    }

    @GetMapping("/api/user/query")
    @ResponseBody
    public User queryUser(Long userId){
        log.info("queryUser userId:{}",userId);
        return userService.queryUser(userId);
    }
}
