package com.example.trade.web.portal;

import com.example.trade.order.Service.LimitBuyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LimitBuyTest {
    @Autowired
    public LimitBuyService limitBuyService;

    @Test
    void addLimitMemberTest(){
        limitBuyService.addLimitMemeber(123456L,668899L);
    }
    @Test
    void isInLimitMemberTest(){
        limitBuyService.isInLimitMember(123456L,668899L);
    }
    @Test
    void removeLimitMemberTest(){
        limitBuyService.removeLimitMember(123456l,668899L);
    }
}
