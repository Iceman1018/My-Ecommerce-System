package com.example.trade.lightningdeal.db.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="deal_activity")
public class DealActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="activity_name")
    private String activityName;
    @Column(name = "goods_id")
    private Long goodsId;
    @Column(name="limit_per_user")
    private Integer limitPerUser;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name="end_time")
    private Date endTime;
    @Column(name="available_stock")
    private Integer availableStock;
    @Column(name="lock_stock")
    private Integer lockStock;
    @Column(name="activity_status")
    private Integer activityStatus;
    @Column(name="deal_price")
    private Integer dealPrice;
    @Column(name="old_price")
    private Integer oldPrice;
    @Column(name="create_time")
    private Date createTime;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName == null ? null : activityName.trim();
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getLimitPerUser(){return limitPerUser;}

    public void setLimitPerUser(Integer limitPerUser){this.limitPerUser=limitPerUser;}

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Integer getLockStock() {
        return lockStock;
    }

    public void setLockStock(Integer lockStock) {
        this.lockStock = lockStock;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Integer getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(Integer dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Integer getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Integer oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}