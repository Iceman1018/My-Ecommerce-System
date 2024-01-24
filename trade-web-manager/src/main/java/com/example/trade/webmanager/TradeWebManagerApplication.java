package com.example.trade.webmanager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@ComponentScan(basePackages = {"com.example"})
@SpringBootApplication
public class TradeWebManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeWebManagerApplication.class, args);
    }

}
