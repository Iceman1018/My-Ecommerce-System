package com.example.trade.webmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.example"})
@MapperScan({"com.example.trade.user.db.mappers","com.example.trade.goods.db.mappers","com.example.trade.order.db.mappers","com.example.trade.lightningdeal.db.mappers"})
@SpringBootApplication
public class TradeWebManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeWebManagerApplication.class, args);
    }

}
