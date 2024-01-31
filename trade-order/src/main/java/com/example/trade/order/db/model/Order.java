package com.example.trade.order.db.model;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="order_info")
public class Order {
    @Id
    private Long id;

    @Column(name="goods_id")
    private Long goodsId;
    @Column(name = "goods_num")
    private Integer goodsNum;
    @Column(name = "pay_price")
    private Integer payPrice;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "activity_id")
    private Long activityId;
    @Column(name = "activity_type")
    private Integer activityType;
    @Column(name = "pay_time")
    private Date payTime;
    @Column(name = "create_time")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Integer getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Integer payPrice) {
        this.payPrice = payPrice;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}