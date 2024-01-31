package com.example.trade.order;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@EnableDiscoveryClient
@EnableRabbit
@ComponentScan(basePackages = {"com.example"})
@SpringBootApplication
public class TradeOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeOrderApplication.class, args);
    }

}
