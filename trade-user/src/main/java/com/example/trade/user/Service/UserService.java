package com.example.trade.user.Service;

import com.example.trade.user.db.model.CartItem;
import com.example.trade.user.db.model.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    User createUser(String userName,String email,String password,String tag);
    User UserLogin(String email, String password);
    User queryUser(Long userId);

}
