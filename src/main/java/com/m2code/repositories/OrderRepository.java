package com.m2code.repositories;

import com.m2code.entities.Order;
import com.m2code.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUser(User user);
}
