package com.example.trade.user.Service.Impl;

import com.example.trade.user.Service.UserService;
import com.example.trade.user.db.dao.UserRepository;
import com.example.trade.user.db.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Slf4j
@Service

public class userServiceImpl implements UserService{

    @Autowired
    private UserRepository userDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        userDao.save(user);
        return user;
    }
    @Override
    public User UserLogin(String email, String password){
        User user=userDao.queryUserByEmail(email);
        if(user==null)
            return null;
        if(bCryptPasswordEncoder.matches(password,user.getLoginPassword())) {
            log.info("Login Successfully");
            return user;
        }else{
            log.info("Login Failed, {}: {}",user.getLoginPassword(),bCryptPasswordEncoder.encode(password));
            return null;
        }
    }

    @Override
    public User queryUser(Long userId){
        User user=userDao.findById(userId).orElse(null);
        return user;
    }


}
