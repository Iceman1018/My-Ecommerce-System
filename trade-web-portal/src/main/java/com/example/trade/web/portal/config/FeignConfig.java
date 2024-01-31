package com.example.trade.web.portal.config;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;

@Configuration
@Slf4j
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor(HttpSession httpSession) {
        return requestTemplate -> {
            log.info("session id:{}",httpSession.getId());
            String sessionId = httpSession.getId();
            requestTemplate.header("Cookie", "JSESSIONID=" + sessionId);
        };
    }
}
