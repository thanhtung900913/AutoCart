package com.n2t.autocart.modules.cart.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Integer itemId,
        Integer productId,
        String productName,
        BigDecimal unitPrice,
        Integer quantity,
        BigDecimal lineTotal
) {
}
