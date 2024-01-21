package com.example.trade.user.Service;

import com.example.trade.user.db.model.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    User createUser(String userName,String email,String password,String tag);

    boolean UserLogin(HttpSession session, String email, String password);
    boolean UserLogin(String email, String password);
    User queryUser(Long userId);

}
