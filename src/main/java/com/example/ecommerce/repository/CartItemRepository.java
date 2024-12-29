package com.example.ecommerce.repository;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem, Long> {
    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.user = :user")
    Optional<Cart> findByUserWithItems(@Param("user") User user);}
