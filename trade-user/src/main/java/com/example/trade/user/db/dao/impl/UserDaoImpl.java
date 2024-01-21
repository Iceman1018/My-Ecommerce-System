package com.example.trade.user.db.dao.impl;

import com.example.trade.user.db.dao.UserDao;
import com.example.trade.user.db.mappers.UserMapper;
import com.example.trade.user.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserDaoImpl implements UserDao{
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean insertUser(User user){
        return userMapper.insert(user)>0;
    }
    public boolean deleteUser(long id){
        return userMapper.deleteByPrimaryKey(id)>0;
    }
    public User queryUser(long id){
        return userMapper.selectByPrimaryKey(id);
    }

    public User queryUserByEmail(String email){
        return userMapper.selectByEmail(email);
    }
    public boolean updateUser(User user){
        return userMapper.updateByPrimaryKey(user)>0;
    }

}
