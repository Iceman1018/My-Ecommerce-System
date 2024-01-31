package com.example.trade.user.db.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="cart_item")
public class CartItem {
    @Id
    private Long id;
    @Column(name="goods_id")
    private Long goodsId;
    @Column(name="goods_title")
    private String goodsTitle;
    @Column(name="goods_img")
    private String goodsImg;
    @Column(name="user_id")
    private Long userId;
    @Column(name="goods_num")
    private Integer goodsNum;
    @Column(name="create_time")
    private Date createTime;
    @Column(name="status")
    private Integer status;
    @Column(name="deal_id")
    private Long dealId;
}
