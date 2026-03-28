package com.n2t.autocart.modules.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.n2t.autocart.modules.account.entity.User;
import com.n2t.autocart.modules.cart.entity.Cart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "fullname", nullable = false, length = 255)
    private String fullname;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "date_join", nullable = false)
    private Instant dateJoin;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "recommend_product_ids", columnDefinition = "integer[]")
    private Integer[] recommendProductIds;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", unique = true)
    @JsonIgnore
    private Cart cart;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private User user;
}
