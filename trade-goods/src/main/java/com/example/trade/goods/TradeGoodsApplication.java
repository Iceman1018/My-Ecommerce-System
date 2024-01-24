package com.example.trade.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.example"})
@MapperScan({"com.example.trade.goods.db.mappers"})
@SpringBootApplication
public class TradeGoodsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeGoodsApplication.class, args);
    }

}
