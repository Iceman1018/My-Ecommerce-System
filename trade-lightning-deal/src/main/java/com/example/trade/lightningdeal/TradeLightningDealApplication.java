package com.example.trade.lightningdeal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableFeignClients
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan(basePackages = {"com.example"})
@MapperScan({"com.example.trade.lightningdeal.db.mappers"})
@SpringBootApplication
public class TradeLightningDealApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeLightningDealApplication.class, args);
    }

}
