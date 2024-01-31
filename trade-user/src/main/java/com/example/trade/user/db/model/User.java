package com.example.trade.user.db.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="user_info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="userName")
    private String userName;
    @Column(name="loginEmail")
    private String loginEmail;
    @Column(name="loginPassword")
    private String loginPassword;
    @Column(name="tags")
    private String tags;
    @OneToMany
    @JoinColumn(name = "user_id")
    private Set<CartItem> cartItems;

    public User(){}
}