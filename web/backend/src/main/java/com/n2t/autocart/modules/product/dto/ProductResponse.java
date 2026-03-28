package com.n2t.autocart.modules.product.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductResponse(
    Integer productId,
    String productName,
    BigDecimal originalPrice,
    Integer stock,
    String brand,
    String slug,
    String productDescription,
    LocalDate dateCreated,
    BigDecimal ratings,
    LocalDate dateAdd,
    Integer categoryId,
    String categoryName,
    Integer vendorId,
    String vendorName
) {
}
