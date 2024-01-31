package com.example.trade.goods.db.model;


import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="number")
    private String number;
    @Column(name="brand")
    private String brand;
    @Column(name = "image")
    private String image;
    @Column(name="description")
    private String description;

    @Column(name="price")
    private Integer price;

    @Column(name="status")
    private Integer status;

    @Column(name="keywords")
    private String keywords;

    @Column(name="category")
    private String category;

    @Column(name="availableStock")
    private Integer availableStock;

    @Column(name="lockStock")
    private Integer lockStock;

    @Column(name="saleNum")
    private Integer saleNum;

    @Column(name="createTime")
    private Date createTime;

    public Goods(){}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number == null ? null : number.trim();
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getKeywords() {
        return keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
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
    public Integer getSaleNum() {
        return saleNum;
    }
    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}