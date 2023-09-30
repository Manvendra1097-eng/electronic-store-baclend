package com.m2code.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "produts")
public class Product {

    @Id
    private String productId;
    @Column(name = "product_title", nullable = false)
    private String title;
    @Column(name = "product_desc", length = 10000)
    private String description;
    private long price;
    private long discountedPrice;
    private Date addedAt;
    private int quantity;
    private boolean live;
    private boolean stock;
    private String productImage;
    @ManyToOne
    private Category category;
    @OneToOne(mappedBy = "product")
    private OrderItem orderItem;
}
