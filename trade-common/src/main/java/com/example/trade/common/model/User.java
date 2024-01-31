package com.example.trade.common.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class User {
    private Long id;

    private String userName;

    private String loginEmail;

    private String loginPassword;

    private Set<CartItem> cartItems;

    private String tags;
}