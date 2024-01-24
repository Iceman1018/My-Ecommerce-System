package com.example.trade.web.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableDiscoveryClient
@EnableRedisHttpSession
@EnableFeignClients
@EnableRabbit
@ComponentScan(basePackages = {"com.example"})
@MapperScan({"com.example.trade.user.db.mappers","com.example.trade.goods.db.mappers","com.example.trade.order.db.mappers","com.example.trade.lightningdeal.db.mappers"})
@SpringBootApplication
public class TradeWebPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeWebPortalApplication.class, args);
    }

}
