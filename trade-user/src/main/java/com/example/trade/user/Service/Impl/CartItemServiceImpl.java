package com.example.trade.user.Service.Impl;

import com.example.trade.user.Service.CartItemService;
import com.example.trade.user.db.dao.CartItemRepository;
import com.example.trade.user.db.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> queryCart(Long userId){
        return cartItemRepository.findByUser(userId);
    }

    public void addCartItem(CartItem cartItem){
        cartItemRepository.save(cartItem);
    }

    public boolean updateCartItemNum(Long itemId, Integer itemNum){
        return cartItemRepository.updateItemNum(itemId,itemNum)>0;
    }

    public void deleteCartItem(Long itemId){
        cartItemRepository.deleteById(itemId);
    }

}
