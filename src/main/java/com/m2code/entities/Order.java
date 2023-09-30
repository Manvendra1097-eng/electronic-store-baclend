package com.m2code.entities;

import com.m2code.dtos.ORDER_STATUS;
import com.m2code.dtos.PAYMENT_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    private String orderId;
    private Date orderDate;
    private Date deliveredDate;
    private long orderAmount;
    @Column(length = 1000)
    private String billingAddress;
    private String billingName;
    private String billingPhone;

    @Enumerated(EnumType.STRING)
    private ORDER_STATUS orderStatus;

    @Enumerated(EnumType.STRING)
    private PAYMENT_STATUS paymentStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
