package com.example.trade.user.Service.Impl;

import com.example.trade.user.Service.UserService;
import com.example.trade.user.db.dao.UserDao;
import com.example.trade.user.db.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.Session;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;


@Slf4j
@Service

public class userServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RedisIndexedSessionRepository redisIndexedSessionRepository;
    @Override
    public User createUser(String userName, String email, String password, String tag){
        if(userDao.queryUserByEmail(email)!=null)
        {
            log.error("This email has been used and cannot be registered again");
            return null;
        }
        User user=new User();
        user.setUserName(userName);
        user.setLoginEmail(email);
        user.setLoginPassword(bCryptPasswordEncoder.encode(password));
        user.setTags("Admin");
        userDao.insertUser(user);
        return user;
    }
    @Override
    public boolean UserLogin(HttpSession session, String email, String password){
        User user=userDao.queryUserByEmail(email);
        log.info("session id:{}",session.getId());
        if(bCryptPasswordEncoder.matches(password,user.getLoginPassword())) {


            session.setAttribute("userName", user.getUserName());
            session.setAttribute("userId", user.getId());
            log.info("Login Successfully");
            return true;
        }else{
            log.info("Login Failed, {}: {}",user.getLoginPassword(),bCryptPasswordEncoder.encode(password));
            return false;
        }
    }
    @Override
    public Long UserLogin(String email, String password){
        User user=userDao.queryUserByEmail(email);
        if(bCryptPasswordEncoder.matches(password,user.getLoginPassword())) {
            log.info("Login Successfully");
            return user.getId();
        }else{
            log.info("Login Failed, {}: {}",user.getLoginPassword(),bCryptPasswordEncoder.encode(password));
            return null;
        }
    }

    @Override
    public User queryUser(Long userId){
        User user=userDao.queryUser(userId);
        return user;
    }

}
