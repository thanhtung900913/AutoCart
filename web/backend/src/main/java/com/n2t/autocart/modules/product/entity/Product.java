package com.n2t.autocart.modules.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.n2t.autocart.modules.cart.entity.Category;
import com.n2t.autocart.modules.profile.entity.Vendor;
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

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(name = "original_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "brand", nullable = false, length = 255)
    private String brand;

    @Column(name = "slug", nullable = false, length = 255)
    private String slug;

    @Column(name = "product_description", nullable = false, columnDefinition = "TEXT")
    private String productDescription;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;

    @Column(name = "ratings", nullable = false, precision = 3, scale = 2)
    private BigDecimal ratings = BigDecimal.ZERO;

    @Column(name = "date_add", nullable = false)
    private LocalDate dateAdd;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    @JsonIgnore
    private Vendor vendor;
}
