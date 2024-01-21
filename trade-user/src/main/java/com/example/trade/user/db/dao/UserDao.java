package com.example.trade.user.db.dao;
import com.example.trade.user.db.model.User;
public interface UserDao {
    boolean insertUser(User user);
    boolean deleteUser(long id);
    User queryUser(long id);

    User queryUserByEmail(String email);
    boolean updateUser(User user);
}
