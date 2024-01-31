package com.example.trade.web.portal.controller;

import com.alibaba.fastjson.JSON;
import com.example.trade.common.model.CartItem;
import com.example.trade.common.model.Goods;
import com.example.trade.common.model.User;
import com.example.trade.common.utils.RedisWorker;
import com.example.trade.web.portal.client.UserFeignClient;
import com.example.trade.web.portal.mq.CartPersistentSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController

public class UserController {
    @Autowired
    private UserFeignClient userService;
    @Autowired
    private CartPersistentSender cartPersistentSender;

    @Autowired
    private RedisWorker redisWorker;

    @RequestMapping("user/{userId}")
    public ResponseEntity<?> userInfo(HttpSession session, @PathVariable long userId){
        log.info("session id:{}",session.getId());
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }else if(!session.getAttribute("userId").equals(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User ID mismatch");
        }else{
            User user=userService.queryUser(userId);
            if(user==null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            return ResponseEntity.ok(user);
        }
    }

    @RequestMapping("/user/login/Action")
    public ResponseEntity<?> userLogin(HttpSession session,
                                       @RequestParam("userEmail") String userEmail,
                                       @RequestParam("userPassword") String userPassword){
        User res=userService.UserLogin(userEmail,userPassword);
        if(res!=null) {
            session.setAttribute("userId",res.getId());
            Map<Long,CartItem>map=new HashMap<>();
            for(CartItem cartItem:res.getCartItems())
                map.put(cartItem.getId(), cartItem);
            session.setAttribute("userCart",map);
            return ResponseEntity.ok(res);
        }
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email and Password didn't match");
    }

    @RequestMapping("/user/register/Action")
    public ResponseEntity<?> userRegister(HttpSession session,
                                          @RequestParam("userName") String userName,
                                          @RequestParam("userEmail") String userEmail,
                                          @RequestParam("userPassword") String userPassword,
                                          @RequestParam("tag") String userTag){
        User user=userService.createUser(userName,userEmail,userPassword,userTag);
        if(user!=null) {
            if(userService.UserLogin(userEmail,userPassword)!=null) {
                session.setAttribute("userId",user.getId());
                log.info("register successfully");
                return ResponseEntity.ok(session.getAttribute("userId"));
            }
            else {
                log.error("Unknown Error");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registered successfully but cannot login");
            }
        }else {
            log.error("Email used");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This email is used, please try another");
        }

    }
    @RequestMapping("/user/logout/Action")
    public ResponseEntity<?> userLogout(HttpSession session){
        if(session.getAttribute("userId")!=null) {
            session.invalidate();
            return ResponseEntity.ok("User logout");
        }
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User didn't login, nothing happened");
    }

}
