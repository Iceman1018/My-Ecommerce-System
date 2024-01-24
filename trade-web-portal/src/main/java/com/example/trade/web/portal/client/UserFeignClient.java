package com.example.trade.web.portal.client;


import com.example.trade.web.portal.client.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpSession;

@FeignClient(name="User-service",contextId="trade-web-portal")
public interface UserFeignClient {
    @RequestMapping("/api/user/create")
    User createUser(@RequestParam("userName") String userName,
                    @RequestParam("email")String email,
                    @RequestParam("password")String password,
                    @RequestParam("tag")String tag);

    @RequestMapping("/api/user/login")
    Long UserLogin(@RequestParam("email")String email,
                      @RequestParam("password")String password);

    @RequestMapping("api/user/query")
    User queryUser(@RequestParam("userId")Long userId);
}



