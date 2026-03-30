package com.n2t.autocart.modules.cart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.n2t.autocart.modules.account.entity.User;
import com.n2t.autocart.modules.profile.entity.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer cartId;

    @Column(name = "grand_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(name = "items_total", nullable = false)
    private Integer itemsTotal = 0;

    @OneToOne(mappedBy = "cart")
    private Customer customer;
}
