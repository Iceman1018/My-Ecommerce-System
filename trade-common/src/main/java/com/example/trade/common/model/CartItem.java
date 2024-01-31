package com.example.trade.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class CartItem implements Serializable {
    private Long id;
    private Long goodsId;
    private String goodsTitle;
    private String goodsImg;
    private Long userId;
    private Integer goodsNum;
    private Date createTime;
    private Integer status;
    private Long dealId;
}
