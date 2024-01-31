package com.example.trade.web.portal.service;


import com.example.trade.common.model.CartItem;

import javax.servlet.http.HttpSession;

public interface SessionService {
    void getCartItem(HttpSession session);
    void createCartItem(HttpSession session, CartItem cartItem);
    void updateCartItem(HttpSession session, Long cartItemId,Integer itemNum);
    void deleteCartItem(HttpSession session, Long cartItemId);

}
