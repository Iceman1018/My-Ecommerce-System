package com.example.trade.user;

import com.example.trade.user.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TradeUserApplicationTests {

    @Autowired
    private UserService userService;
    @Test
    void CreateUserTest() {
        userService.createUser("Iceman1018","zhaoheng1018@gmail.com","001018Pu@","Admin");
    }
    @Test
    void LoginTest() {
        userService.UserLogin("zhaoheng1018@gmail.com","001018Pu");
    }

}
