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

    public boolean itemInvalidationByDealId(Long dealId){
        return cartItemRepository.itemInvalidationByDealId(dealId)>0;
    }

    public boolean itemInvalidationByGoodsId(Long goodsId){
        return cartItemRepository.itemInvalidationByGoodsId(goodsId)>0;
    }

    public List<CartItem> itemInvalidationFind(){
        return cartItemRepository.itemInvalidationFind();
    }

    public List<CartItem> findByDealId(Long dealId){
        return cartItemRepository.findByDealId(dealId);
    }
    public List<CartItem> findByGoodsId(Long goodsId){
        return cartItemRepository.findByGoodsId(goodsId);
    }

}
