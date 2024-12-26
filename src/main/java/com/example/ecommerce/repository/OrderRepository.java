package com.example.ecommerce.repository;


import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    List<Order> findByUserAndStatus(User user, OrderStatus orderStatus);

    List<Order> findByStatus(OrderStatus orderStatus);
}
