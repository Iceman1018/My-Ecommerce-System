package com.example.trade.user.db.dao;

import com.example.trade.user.db.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("FROM User WHERE loginEmail = :email")
    User queryUserByEmail(String email);
}
