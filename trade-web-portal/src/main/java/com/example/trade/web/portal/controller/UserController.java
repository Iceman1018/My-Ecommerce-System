package com.example.trade.web.portal.controller;

import com.example.trade.user.Service.UserService;
import com.example.trade.user.db.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("user/{userId}")
    public ResponseEntity<?> userInfo(HttpSession session, @PathVariable long userId){
        if(session.getAttribute("userId")==null){
            log.error("You haven't logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
        }else if(session.getAttribute("userId").equals(userId)) {
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
        boolean res=userService.UserLogin(session,userEmail,userPassword);
        if(res)
            return ResponseEntity.ok(session.getAttribute("userId"));
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
            if(userService.UserLogin(session,userEmail,userPassword)) {
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
}
