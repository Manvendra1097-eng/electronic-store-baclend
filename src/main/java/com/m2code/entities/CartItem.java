package com.m2code.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartItemId;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    private long totalPrice;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
