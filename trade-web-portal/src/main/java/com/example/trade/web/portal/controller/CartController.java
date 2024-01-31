package com.example.trade.web.portal.controller;
import com.example.trade.common.model.CartItem;
import com.example.trade.web.portal.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@Slf4j
@RestController
@ComponentScan()
public class CartController {
    @Autowired
    private SessionService sessionService;
    @RequestMapping("cart/check")
    public ResponseEntity<?> getCart(HttpSession session){
        if(session.getAttribute("userId")!=null) {
            sessionService.getCartItem(session);
            return ResponseEntity.ok(session.getAttribute("userCart"));
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
    }

    @RequestMapping("cart/update/{cartItemId}/{itemNum}")
    public ResponseEntity<?> updateCart(HttpSession session,
                                        @PathVariable("cartItemId") Long cartItemId,
                                        @PathVariable("itemNum") Integer itemNum){
        if(session.getAttribute("userId")!=null) {
            sessionService.updateCartItem(session,cartItemId,itemNum);
            return ResponseEntity.ok(session.getAttribute("userCart"));
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
    }

    @RequestMapping("cart/create")
    public ResponseEntity<?> createCart(HttpSession session,
                                        @RequestBody CartItem cartItem){
        if(session.getAttribute("userId")!=null) {
            sessionService.createCartItem(session,cartItem);
            return ResponseEntity.ok(session.getAttribute("userCart"));
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
    }

    @RequestMapping("cart/delete/{cartItemId}")
    public ResponseEntity<?> deleteCart(HttpSession session,
                                        @PathVariable("cartItemId")Long cartItemId){
        if(session.getAttribute("userId")!=null) {
            sessionService.deleteCartItem(session,cartItemId);
            return ResponseEntity.ok(session.getAttribute("userCart"));
        }
        else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in");
    }
}
