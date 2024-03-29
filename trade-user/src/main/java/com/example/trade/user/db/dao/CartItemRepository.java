package com.example.trade.user.db.dao;

import com.example.trade.user.db.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query("FROM CartItem WHERE userId = :userId")
    List<CartItem> findByUser(Long userId);
    @Modifying
    @Transactional
    @Query("UPDATE CartItem i SET i.goodsNum = :num WHERE i.id = :id")
    int updateItemNum(long id, int num);

    @Modifying
    @Transactional
    @Query("UPDATE CartItem i SET i.status = 0 WHERE i.goodsId = :goodId")
    int itemInvalidationByGoodsId(long goodId);

    @Modifying
    @Transactional
    @Query("UPDATE CartItem i SET i.status = 0 WHERE i.dealId = :dealId")
    int itemInvalidationByDealId(long dealId);

    @Query("FROM CartItem WHERE status = 0")
    List<CartItem> itemInvalidationFind();

    @Query("FROM CartItem WHERE dealId = :dealId")
    List<CartItem> findByDealId(Long dealId);

    @Query("FROM CartItem WHERE goodsId = :goodsId")
    List<CartItem> findByGoodsId(Long goodsId);



}
