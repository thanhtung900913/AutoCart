package com.n2t.autocart.modules.profile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.n2t.autocart.modules.account.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_methods")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_method_id")
    private Integer paymentMethodId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "provider", nullable = false, length = 100)
    private String provider;

    @Column(name = "masked_account_number", nullable = false, length = 50)
    private String maskedAccountNumber;

    @Column(name = "card_holder_name", length = 255)
    private String cardHolderName;

    @Column(name = "expires_at")
    private LocalDate expiresAt;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
