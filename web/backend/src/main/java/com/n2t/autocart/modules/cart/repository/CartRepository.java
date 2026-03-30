package com.n2t.autocart.modules.cart.repository;

import com.n2t.autocart.modules.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByCustomer_User_Email(String email);
}
