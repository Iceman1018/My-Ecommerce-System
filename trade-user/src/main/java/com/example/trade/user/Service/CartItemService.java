package com.example.trade.user.Service;

import com.example.trade.user.db.model.CartItem;

import java.util.List;

public interface CartItemService {
    List<CartItem> queryCart(Long userId);

    void addCartItem(CartItem cartItem);

    boolean updateCartItemNum(Long itemId, Integer itemNum);

    void deleteCartItem(Long itemId);

    boolean itemInvalidationByDealId(Long dealId);

    boolean itemInvalidationByGoodsId(Long goodsId);

    List<CartItem> itemInvalidationFind();
    List<CartItem> findByDealId(Long dealId);
    List<CartItem> findByGoodsId(Long goodsId);
}
